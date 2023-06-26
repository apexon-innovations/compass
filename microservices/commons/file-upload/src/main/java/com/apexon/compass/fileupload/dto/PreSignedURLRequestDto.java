package com.apexon.compass.fileupload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreSignedURLRequestDto {

    private String blobName; // similar to bucket name in s3

    private String key; // path to the object

}
