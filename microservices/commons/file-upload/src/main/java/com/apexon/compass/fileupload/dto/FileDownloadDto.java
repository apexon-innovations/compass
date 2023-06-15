package com.apexon.compass.fileupload.dto;

import com.amazonaws.regions.Regions;

public class FileDownloadDto {

    private Regions region;

    private String bucketName;

    private String path;

    private String regionString;

    public FileDownloadDto(String regionString, String bucketName, String path) {
        this.regionString = regionString;
        this.bucketName = bucketName;
        this.path = path;
    }

    public FileDownloadDto(Regions region, String bucketName, String path) {
        this.region = region;
        this.bucketName = bucketName;
        this.path = path;
    }

    public String getRegionString() {
        return regionString;
    }

    public void setRegionString(String regionString) {
        this.regionString = regionString;
    }

    public Regions getRegion() {
        return region;
    }

    public void setRegion(Regions region) {
        this.region = region;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
