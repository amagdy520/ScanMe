package com.scan.me.CustomerSubmittion;

/**
 * Created by Ahmed Magdy on 2/19/2018.
 */

public class SubmitAdapter {
    private String name;
    private String username;
    private String email;
    private String image;
    private String attend;

    public SubmitAdapter() {
    }

    public SubmitAdapter(String name, String username, String email, String image, String attend) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.image = image;
        this.attend = attend;
    }

    public String getAttend() {
        return attend;
    }

    public void setAttend(String attend) {
        this.attend = attend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
