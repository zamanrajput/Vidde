package com.devphics.vidde.ModelClasses;

public class webdata {
    String size;
    String type;
    String link;
    String name;
    String page;
    String website;
    boolean chunked = false;
    boolean checked = false;
    boolean expanded = false;
    boolean audio;

    public webdata(String size, String type, String link, String name, String page, String website, boolean chunked, boolean checked, boolean expanded, boolean audio) {
        this.size = size;
        this.type = type;
        this.link = link;
        this.name = name;
        this.page = page;
        this.website = website;
        this.chunked = chunked;
        this.checked = checked;
        this.expanded = expanded;
        this.audio = audio;
    }

    public webdata(String size, String type, String link, String name, String page, boolean chunked, String website, boolean audio) {

    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isChunked() {
        return chunked;
    }

    public void setChunked(boolean chunked) {
        this.chunked = chunked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }
}
