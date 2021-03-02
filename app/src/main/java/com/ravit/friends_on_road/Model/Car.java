package com.ravit.friends_on_road.Model;

public class Car {
    private String model;
    private String year;
    private String engine;
    private String places;
    private String licensePlateNum;
    private String emailOwner;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public String getModel() { return model; }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    public String getYear() { return year; }

    public String getEngine() { return engine; }

    public String getPlaces() { return places; }

    public String getEmailOwner() { return emailOwner; }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public void setEmailOwner(String emailOwner) {
        this.emailOwner = emailOwner;
    }


}
