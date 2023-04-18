package com.example.colabed.service;

import com.example.colabed.api.model.Cursors;
import com.example.colabed.api.model.Room;
import com.example.colabed.api.model.Roomrepository;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

@Service

public class Roomservice {
    private final Roomrepository roomrepository;
    public Roomservice(Roomrepository roomrepository){this.roomrepository=roomrepository;}
    public int validRoomCode(String roomCode)
    {
        Optional<Room> r=roomrepository.findById(roomCode);
        if (r.isPresent())
        {
            System.out.println("hello");
            return 1;
        }
        else {
            return 0;
        }

    }
    public Optional<Room> findRoom(String roomCode)
    {
        Optional<Room> r= roomrepository.findById(roomCode);
        return r;
    }
    public Room updateRoom(Room room)

    {


        return roomrepository.save(room);
    }
    public Room newRoom(String roomName, String defaultLanguage,String email)
    {
        Map<String,Cursors> members= new Hashtable<>();
        Room r = roomrepository.findRoomByRoomName(roomName);
        if(r!=null)
        {
            return null;
        }
        else {
            Cursors cursors = new Cursors();
            cursors.python="0:0";
            cursors.javascript="0:0";
            cursors.cpp="0:0";

            members.put(email,cursors);
            String roomCode="";
            StringBuilder builder = new StringBuilder();
            final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            while(true) {
                final SecureRandom secureRandom = new SecureRandom();

                for (int i = 0; i < 6; i++) {
                    int randomIndex = secureRandom.nextInt(ALPHA_NUMERIC_STRING.length());
                    char randomChar = ALPHA_NUMERIC_STRING.charAt(randomIndex);
                    builder.append(randomChar);

                }
                roomCode = builder.toString();
                Optional<Room> room1= roomrepository.findById(roomCode);
                if (room1.isEmpty())
                {
                    break;
                }
            }
            Room room= new Room(roomName,roomCode,defaultLanguage,null,email,members);
            roomrepository.save(room);
            return room;
        }
    }
}
