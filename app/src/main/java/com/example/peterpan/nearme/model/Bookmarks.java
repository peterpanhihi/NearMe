package com.example.peterpan.nearme.model;

/**
 * Created by Peterpan on 5/13/2016 AD.
 */
public class Bookmarks {
    private String name;
    private double latitude;
    private double longitude;
    private String phone_number;
    private String address;
    private String type;
    private String user_id;
    private String place_id;

    public Bookmarks() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    @Override
    public String toString() {
        return "Bookmarks{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", phone_number='" + phone_number + '\'' +
                ", type='" + type + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
