package com.devphics.vidde.ModelClasses;

public class StatusModel {

    private String name,fileUri;

    public StatusModel(String name, String fileUri) {
        this.name = name;
        this.fileUri = fileUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }
}
