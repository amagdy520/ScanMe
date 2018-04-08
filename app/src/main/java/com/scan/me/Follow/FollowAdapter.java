package com.scan.me.Follow;

/**
 * Created by Ahmed Magdy on 2/19/2018.
 */

public class FollowAdapter {
    private String name;
    private String username;
    private String image;
    public FollowAdapter() {
    }

    public FollowAdapter(String name, String username, String image) {
        this.name = name;
        this.username = username;
        this.image = image;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

