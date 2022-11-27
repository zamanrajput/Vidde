package com.devphics.vidde.ModelClasses;

import android.net.Uri;

import java.net.URI;

public class DownloadModel {
    String title;
    String mimeType;
    String data;
    long id;
    boolean isPaused;
    int progress;
    int totalSize;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public DownloadModel(String title, String mimeType, String data, long id, boolean isPaused, int progress, int totalSize) {
        this.title = title;
        this.mimeType = mimeType;
        this.data = data;
        this.id = id;
        this.isPaused = isPaused;
        this.progress = progress;
        this.totalSize = totalSize;
    }
}
