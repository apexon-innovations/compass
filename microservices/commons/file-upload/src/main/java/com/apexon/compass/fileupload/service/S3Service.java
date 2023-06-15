package com.apexon.compass.fileupload.service;

import static com.apexon.compass.constants.DesFileUploadConstants.BASE64;
import static com.apexon.compass.constants.DesFileUploadConstants.CONTENT_TYPE_IMAGE;
import static com.apexon.compass.constants.DesFileUploadConstants.SETCONTROLCACHE;
import static com.apexon.compass.constants.PsrServiceConstants.FULLSTOP_PUNCTUATION;
import static com.apexon.compass.constants.PsrServiceConstants.PATH_SEPERATER;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.apexon.compass.fileupload.dto.BlobObjectMetadata;
import com.apexon.compass.fileupload.dto.FileDownloadDto;
import com.apexon.compass.fileupload.dto.FileUploadDto;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

import com.apexon.compass.fileupload.dto.PreSignedURLRequestDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Service implements UploadService {

    private final AmazonS3 s3Client;

    private final Environment environment;

    @Override
    public void uploadToStorage(FileUploadDto fileUploadDto) throws SdkClientException {
        s3Client.putObject(fileUploadDto.getBucketName(), fileUploadDto.getPath(), fileUploadDto.getFile());
    }

    @Override
    public BlobObjectMetadata downloadFromStorage(FileDownloadDto fileDownloadDto) throws SdkClientException {
        BlobObjectMetadata blobObjectMetadata = new BlobObjectMetadata();
        S3Object s3Object = s3Client.getObject(fileDownloadDto.getBucketName(), fileDownloadDto.getPath());
        blobObjectMetadata.setUri(s3Object.getObjectContent().getHttpRequest().getURI().toString());
        blobObjectMetadata.setKey(s3Object.getKey());
        return blobObjectMetadata;
    }

    @Override
    public void uploadBase64ImageToS3(FileUploadDto fileUploadDto) throws SdkClientException {
        byte[] imageByte = Base64.decodeBase64(fileUploadDto.getFile().getBytes());
        InputStream inputStream = new ByteArrayInputStream(imageByte);
        String fileName = fileUploadDto.getPath().substring(fileUploadDto.getPath().indexOf(PATH_SEPERATER) + 1);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageByte.length);
        metadata.setContentType(
                String.format(CONTENT_TYPE_IMAGE, fileName.substring(fileName.indexOf(FULLSTOP_PUNCTUATION) + 1)));
        metadata.setContentEncoding(BASE64);
        metadata.setCacheControl(SETCONTROLCACHE);
        PutObjectRequest request = new PutObjectRequest(fileUploadDto.getBucketName(), fileUploadDto.getPath(),
                inputStream, metadata);
        s3Client.putObject(request);
    }

    @Override
    public String generatePresignedUrl(PreSignedURLRequestDto requestDto) throws SdkClientException {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += (1000 * 60 * 60) / 4;
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                requestDto.getBlobName(), requestDto.getKey())
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration);
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    @Override
    public void uploadPsrFile(FileUploadDto fileUploadDto) throws SdkClientException {
        s3Client.putObject(fileUploadDto.getBucketName(), fileUploadDto.getPath(), fileUploadDto.getFiles());
    }

    @Override
    public boolean checkIfFileExist(String bucketName, String objectName) throws SdkClientException {
        return s3Client.doesObjectExist(bucketName, objectName);
    }

    @Override
    public String getBlobRegion() {
        String defaultRegion = "us-east-2";
        return Objects.isNull(environment.getProperty("AWS_REGION")) ? defaultRegion
                : environment.getProperty("AWS_REGION");
    }

}
