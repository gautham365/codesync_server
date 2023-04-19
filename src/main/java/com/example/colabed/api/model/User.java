package com.example.colabed.api.model;

import java.util.ArrayList;

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

    private String socketId;
    private ArrayList<String> pastRooms;

//    private String username;

    public User(String id, String email,String accessToken, String name, String photo, String socketId, ArrayList<String> pastRooms) {
        this.email=email;
        this.id = id;
        this.accessToken = accessToken;
        this.name = name;
        this.photo = photo;
        this.socketId=socketId;
        this.pastRooms=pastRooms;
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

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public ArrayList<String> getPastRooms() {
        return pastRooms;
    }

    public void setPastRooms(ArrayList<String> pastRooms) {
        this.pastRooms = pastRooms;
    }
}
