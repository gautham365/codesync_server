package com.example.colabed.api.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;

public class Room {

    private String roomName;
    @Id
    private String roomCode;

    private String defaultLanguage;

    private Data data;
    private String owner;

    private Map<String,Cursors> members;

    public Room(String roomName, String roomCode, String defaultLanguage, Data data, String owner, Map<String, Cursors> members) {
        this.roomName = roomName;
        this.roomCode = roomCode;
        this.defaultLanguage = defaultLanguage;
        this.data = data;
        this.owner = owner;
        this.members = members;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public Data getData() {
        return data;
    }

    public String getOwner() {
        return owner;
    }

    public Map<String, Cursors> getMembers() {
        return members;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setMembers(Map<String, Cursors> members) {
        this.members = members;
    }
}
