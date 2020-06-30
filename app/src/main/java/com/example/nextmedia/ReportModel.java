package com.example.nextmedia;

public class ReportModel {
    private String image,name;

    public ReportModel(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public ReportModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
