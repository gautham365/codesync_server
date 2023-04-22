package com.example.colabed.service;

import com.example.colabed.api.model.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.*;

@Service

public class Roomservice {
    private final Roomrepository roomrepository;
    private final Userservice userservice;
    public Roomservice(Roomrepository roomrepository, Userservice userservice){
        this.roomrepository=roomrepository;
        this.userservice = userservice;
    }
    public int validRoomCode(String roomCode)
    {
        Optional<Room> r=roomrepository.findById(roomCode);
        if (r.isPresent())
        {
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
        String str="";
        for(int i =0;i<email.length();i++)
        {
            if (email.charAt(i)=='.')
            {
                str=str+('?');
            }
            else
            {
                str=str+email.charAt(i);
            }

        }
        System.out.println(str);
        Optional<Room> r = Optional.ofNullable( roomrepository.findRoomByRoomName(roomName,str));


        if(r.isPresent())
        {
            System.out.println("wrld");
            return null;
//            if (r.getOwner().equals(email)) {
//                return null;
//            }
//            else
//            {
//                Cursors cursors = new Cursors();
//                cursors.python="0:0";
//                cursors.javascript="0:0";
//                cursors.cpp="0:0";
//
//                members.put(email,cursors);
//                String roomCode="";
//                StringBuilder builder = new StringBuilder();
//                final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//                while(true) {
//                    final SecureRandom secureRandom = new SecureRandom();
//
//                    for (int i = 0; i < 6; i++) {
//                        int randomIndex = secureRandom.nextInt(ALPHA_NUMERIC_STRING.length());
//                        char randomChar = ALPHA_NUMERIC_STRING.charAt(randomIndex);
//                        builder.append(randomChar);
//
//                    }
//                    roomCode = builder.toString();
//                    Optional<Room> room1= roomrepository.findById(roomCode);
//                    if (room1.isEmpty())
//                    {
//                        break;
//                    }
//                }
//                Room room= new Room(roomName,roomCode,defaultLanguage,null,email,members);
//                roomrepository.save(room);
//                return room;
//            }
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

    public ArrayList<PastRoom> getPastRoomsWithRoomNames(String token) throws Exception {
        Optional<User> user = userservice.getUserByToken(token);

        ArrayList<PastRoom> data = new ArrayList<>();
        if (user.isEmpty()){
            throw new Exception("User not available");
        }
        for (String roomCode : user.get().getPastRooms()) {
            Optional<Room> room = findRoom(roomCode);
            if (room.isEmpty()){
                continue;
            }
            String roomName = room.get().getRoomName();
            data.add(new PastRoom(roomCode,roomName));
        }
        return data;
    }

    public User addUserToRoom(String token, String roomCode) throws Exception {
        Optional<User> user= userservice.getUserByToken(token);
        if (user.isEmpty()){
            throw new Exception("Unauthorised");
        }
        User u = user.get();
        Map<String,Cursors> members;
        Cursors cursors = new Cursors();
        cursors.python="0:0";
        cursors.javascript="0:0";
        cursors.cpp="0:0";
        Optional<Room> o=findRoom(roomCode);
        if (o.isEmpty()){
            throw new Exception("Invalid Room Code");
        }
        Room r=o.get();
        members = r.getMembers();
        if (members.containsKey(u.getEmail())){
            return u;
        }
            String email=u.getEmail();
            members.put(email,cursors);
            r.setMembers(members);
            Room room = updateRoom(r);

            return u;
    }

    public void removeUserFromRoom(String token, String roomCode) throws Exception {
        Optional<User> user= userservice.getUserByToken(token);
        if (user.isEmpty()){
            throw new Exception("Unauthorised");
        }
        User u = user.get();
        Map<String,Cursors> members;
        Cursors cursors = new Cursors();
        cursors.python="0:0";
        cursors.javascript="0:0";
        cursors.cpp="0:0";
        Optional<Room> o=findRoom(roomCode);
        if (o.isEmpty()){
            throw new Exception("Invalid Room Code");
        }
        Room r=o.get();
        members = r.getMembers();
        if (!members.containsKey(u.getEmail())){
            return;
        }
        String email=u.getEmail();
        members.remove(email);
        r.setMembers(members);
        Room room = updateRoom(r);
    }

    public User updateCursorOfUser(String roomCode, String language, String cursor, String token) throws Exception {
        Optional<User> user= userservice.getUserByToken(token);
        if (user.isEmpty()){
            throw new Exception("Unauthorised");
        }
        User u = user.get();
        Optional<Room> o=findRoom(roomCode);
        if (o.isEmpty()){
            throw new Exception("Invalid Room Code");
        }
        Room r=o.get();
        Map<String,Cursors> members;
        members = r.getMembers();
        if (!members.containsKey(u.getEmail())){
            return u;
        }
        String email=u.getEmail();
        Cursors cursors = new Cursors();
        cursors = members.get(email);

        Field field = Cursors.class.getDeclaredField(language);
        field.set(cursors, cursor);

        members.put(email,cursors);
        r.setMembers(members);
        Room room = updateRoom(r);
        return u;
    }

}
