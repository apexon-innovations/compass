package com.apexon.compass.fileupload.service;

import com.amazonaws.SdkClientException;
import com.apexon.compass.fileupload.dto.BlobObjectMetadata;
import com.apexon.compass.fileupload.dto.FileDownloadDto;
import com.apexon.compass.fileupload.dto.FileUploadDto;
import com.apexon.compass.fileupload.dto.PreSignedURLRequestDto;

public interface UploadService {

    void uploadToStorage(FileUploadDto fileUploadDto) throws SdkClientException;

    BlobObjectMetadata downloadFromStorage(FileDownloadDto fileDownloadDto) throws SdkClientException;

    void uploadBase64ImageToS3(FileUploadDto fileUploadDto) throws SdkClientException;

    String generatePresignedUrl(PreSignedURLRequestDto s3Object) throws SdkClientException;

    void uploadPsrFile(FileUploadDto fileUploadDto) throws SdkClientException;

    boolean checkIfFileExist(String bucketName, String objectName) throws SdkClientException;

    String getBlobRegion();

}
