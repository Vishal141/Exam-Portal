package com.exam.portal.models;

import java.io.File;

public class Option {
    private boolean isImage;
    private String text;
    private File file;
    private int index;

    public Option(){}

    public Option(String text){
        this.text = text;
        this.isImage = false;
    }

    public Option(File file){
        this.file = file;
        this.isImage = true;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
