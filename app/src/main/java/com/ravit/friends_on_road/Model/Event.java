package com.ravit.friends_on_road.Model;

public class Event {

    private String emailOwner;
    private String eventNum;
    private String type;
    private String description;
    private String locaion;
    private String car;
    private String status="open";
    private String imgUrl;

    public String getEventNum() {
        return eventNum;
    }

    public void setEventNum(String eventNum) {
        this.eventNum = eventNum;
    }

    public String getEmailOwner() {
        return emailOwner;
    }

    public void setEmailOwner(String emailOwner) {
        this.emailOwner = emailOwner;
    }



    private String ownerEmail;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocaion(String locaion) {
        this.locaion = locaion;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getLocaion() {
        return locaion;
    }

    public String getCar() {
        return car;
    }
}
