package com.apexon.compass.fileupload.dto;

import com.amazonaws.regions.Regions;

import java.io.File;

public class FileUploadDto {

    private Regions region;

    private String bucketName;

    private String path;

    private String file;

    private File files;

    private String regionString;

    public FileUploadDto(String regionString, String bucketName, String path, String file) {
        this.regionString = regionString;
        this.bucketName = bucketName;
        this.path = path;
        this.file = file;
    }

    public FileUploadDto(String regionString, String bucketName, String path, File files) {
        this.regionString = regionString;
        this.bucketName = bucketName;
        this.path = path;
        this.files = files;
    }

    public FileUploadDto(Regions region, String bucketName, String path, String file) {
        this.region = region;
        this.bucketName = bucketName;
        this.path = path;
        this.file = file;
    }

    public FileUploadDto(Regions region, String bucketName, String path, File files) {
        this.region = region;
        this.bucketName = bucketName;
        this.path = path;
        this.files = files;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public File getFiles() {
        return files;
    }

    public void setFiles(File files) {
        this.files = files;
    }

}
