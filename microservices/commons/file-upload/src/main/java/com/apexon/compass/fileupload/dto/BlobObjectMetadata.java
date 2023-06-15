package com.apexon.compass.fileupload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlobObjectMetadata {

    private String uri;

    private String key;

    private String blobName;

}
