package com.ravit.friends_on_road.Model;

public class Event {

    private String emailOwner;

    private String numOfSpecificEvent;
    private String type;
    private String description;
    private String locaion;
    //private String car;
    private String status="open";
    private String imgUrl;



    public String getNumOfSpecificEvent() {
        return numOfSpecificEvent;
    }

    public void setNumOfSpecificEvent(String numOfSpecificEvent) {
        this.numOfSpecificEvent = numOfSpecificEvent;
    }

    public String getEmailOwner() {
        return emailOwner;
    }

    public void setEmailOwner(String emailOwner) {
        this.emailOwner = emailOwner;
    }





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

    public void openEvent() {
        this.status = "open";
    }

    public void closeEvent(){this.status="close";}


    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocaion(String locaion) {
        this.locaion = locaion;
    }

//    public void setCar(String car) {
//        this.car = car;
//    }

    public String getLocaion() {
        return locaion;
    }

//    public String getCar() {
//        return car;
//    }
}
