package com.ravit.friends_on_road.Model;

import java.util.List;

public class User {

    private String name;
    //private String id;
    private String phone;
    private String email;
    private String password;
    private int[] carList = new int[10];//License plates numbers
    private int carAmount=0;
    int[] arr = new int[100];
    private String imageUrl;


    public String getName() {
        return name;
    }

    public String getPhone() { return phone; }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public int[] getAllCars() { return carList; }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void addCar(int newCarNum) {
        carList[carAmount]=newCarNum;
        carAmount++;
    }




}
