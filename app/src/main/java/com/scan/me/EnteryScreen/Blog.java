package com.scan.me.EnteryScreen;

/**
 * Created by Ahmed Magdy on 2/16/2018.
 */

public class Blog {
    private String name;
    private String usname;
    private String username;
    private String uid;
    private String details;
    private String date;
    private String address;
    private String image;

    public Blog() {
    }

    public Blog(String name, String usname, String username, String uid, String details, String date, String address, String image) {
        this.name = name;
        this.usname = usname;
        this.username = username;
        this.uid = uid;
        this.details = details;
        this.date = date;
        this.address = address;
        this.image = image;
    }

    public String getUsname() {
        return usname;
    }

    public void setUsname(String usname) {
        this.usname = usname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
