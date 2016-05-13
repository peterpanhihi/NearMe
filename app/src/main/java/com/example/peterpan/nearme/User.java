package com.example.peterpan.nearme;

import java.io.Serializable;

/**
 * Created by Peterpan on 5/13/2016 AD.
 */
public class User implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private String name;
    private String imageUrl;
    private String token;
    private Bookmarks bookmarks;

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public String getToken() {
        return token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
