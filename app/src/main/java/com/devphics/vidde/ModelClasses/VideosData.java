package com.devphics.vidde.ModelClasses;

public class VideosData {
    String title;
    String mimeType;
    long size;
    String image;
    String data;
    String uri;
    long ID;
    long duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public VideosData(String title, String mimeType, long size, String image, String data, String uri, long ID, long duration) {
        this.title = title;
        this.mimeType = mimeType;
        this.size = size;
        this.image = image;
        this.data = data;
        this.uri = uri;
        this.ID = ID;
        this.duration = duration;
    }
}
