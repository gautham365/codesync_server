package com.example.colabed.api.model;

public class User
{
//    private Integer id;

     private String id;

    public User() {
    }

    private String email;

      private String accessToken;
    private String name;
    private String photo;

//    private String username;

    public User(String id, String email,String accessToken, String name, String photo) {
        this.email=email;
        this.id = id;
        this.accessToken = accessToken;
        this.name = name;
        this.photo = photo;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
