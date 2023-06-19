package com.apexon.compass.psrservice.service.impl.helper;

import com.amazonaws.SdkClientException;
import com.apexon.compass.entities.Project;
import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.fileupload.dto.BlobObjectMetadata;
import com.apexon.compass.fileupload.dto.FileDownloadDto;
import com.apexon.compass.fileupload.dto.PreSignedURLRequestDto;
import com.apexon.compass.fileupload.service.UploadService;
import com.apexon.compass.psrservice.config.properties.JwtSecretProperty;
import com.apexon.compass.psrservice.dao.ProjectDetailsDao;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.apexon.compass.constants.PsrServiceConstants.*;

@Slf4j
@Component
@AllArgsConstructor
public class ProjectHelper {

    private ProjectDetailsDao projectDetailsDao;

    private UploadService uploadService;

    private Environment environment;

    public Project findAndValidateProjectByIdAndUser(String id) throws UnauthorizedException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Project projects = projectDetailsDao.findAssociatedUsersWithProject(id, username);
        if (projects == null) {
            throw new ForbiddenException("Invalid access");
        }
        return projects;
    }

    public List<String> getWidget() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Arrays.asList(jwt.getClaimAsString("wdgt").split(","));
    }

    public String getPresignedURL(String fileName) throws SdkClientException {
        if (StringUtils.isNotEmpty(fileName)) {
            StringBuilder path = new StringBuilder();
            path.append(getBucketName()).append(PATH_SEPERATER).append(fileName);
            FileDownloadDto fileDownloadDto = new FileDownloadDto(
                    Objects.isNull(environment.getProperty("AWS_REGION")) ? "us-east-2"
                            : environment.getProperty("AWS_REGION"),
                    String.valueOf(getBucketName()), String.valueOf(path));
            BlobObjectMetadata s3Object = uploadService.downloadFromStorage(fileDownloadDto);
            PreSignedURLRequestDto requestDto = new PreSignedURLRequestDto(s3Object.getBlobName(), s3Object.getKey());
            return uploadService.generatePresignedUrl(requestDto);
        }
        return org.apache.commons.lang3.StringUtils.EMPTY;
    }

    public StringBuilder getBucketName() {
        StringBuilder bucketName = new StringBuilder();
        if (Arrays.stream(environment.getActiveProfiles()).count() != 0
                || Arrays.stream(environment.getDefaultProfiles()).count() == 0) {
            return bucketName.append(
                    String.format(BUCKETNAME, Arrays.stream(environment.getActiveProfiles()).findAny().orElse(null)));
        }
        else {
            throw new ServiceException("No profile found");
        }
    }

    public File covertBase64ToFile(String base64, String fileName, String fileType) {
        DatatypeConverter.parseBase64Binary(String.valueOf(base64.getBytes()));
        BufferedImage image = null;
        byte[] imageByte = Base64.decodeBase64(base64.getBytes());
        InputStream byteArrayInputStream = new ByteArrayInputStream(imageByte);
        try {
            if (!fileType.contains(SVG_TYPE)) {
                image = ImageIO.read(byteArrayInputStream);
            }
            else {
                TranscoderInput transcoderInput = new TranscoderInput(byteArrayInputStream);
                ByteArrayOutputStream pngOstream = new ByteArrayOutputStream();
                TranscoderOutput outputPngImage = new TranscoderOutput(pngOstream);
                PNGTranscoder myConverter = new PNGTranscoder();
                myConverter.transcode(transcoderInput, outputPngImage);
                byte[] pngByte = pngOstream.toByteArray();
                image = ImageIO.read(new ByteArrayInputStream(pngByte));
                pngOstream.flush();
                pngOstream.close();
            }
            if (image == null) {
                throw new ServiceException("Image is not available");
            }
            log.info("width-->" + image.getWidth() + "Height-->" + image.getHeight());
            byteArrayInputStream.close();
            /*
             * TODO @Jay Why do we use mock multipart file? This is specifically for
             * testing. Make sure to remove this and pom spring-web-mock
             */
            MultipartFile multipartFile = new MockMultipartFile(fileName, DatatypeConverter.parseBase64Binary(base64));
            if (multipartFile.getSize() >= 1024 * 1024) {
                throw new ServiceException("File size too large");
            }
        }
        catch (IllegalArgumentException e) {
            throw new ServiceException("No base64 String");
        }
        catch (IOException e) {
            throw new RecordNotFoundException("File not found");
        }
        catch (TranscoderException e) {
            throw new ServiceException("Error while reading svg File");
        }
        return new File(fileName);
    }

}
