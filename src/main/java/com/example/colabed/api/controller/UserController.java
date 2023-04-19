package com.example.colabed.api.controller;

import com.example.colabed.api.model.*;
import com.example.colabed.config.WebSocketCon;
import com.example.colabed.config.WebSocketConfig;
import com.example.colabed.service.Roomservice;
import com.example.colabed.service.Userservice;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONFilter;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
public class UserController
{
    private Userservice userservice;
    private Roomservice roomservice;
    @Autowired

    public UserController(Userservice userservice,Roomservice roomservice) {

        this.userservice=userservice;
        this.roomservice=roomservice;

    }
    @GetMapping("/")
    public String getUser() {

        return "Code Sync Server";

    }


    @PostMapping("/user")
    @ResponseStatus(value=HttpStatus.OK)
    public void Loginuser()throws Exception{}

    @PostMapping("/login")
    @ResponseStatus(value= HttpStatus.OK)
    public  User createUser(@RequestBody User user)
    {

        StringBuilder email= new StringBuilder();
        String em=user.getEmail();
        for(int i =0; i<em.length();i++)
        {

            if (em.charAt(i)=='.')
            {
                email.append("?");
            }
            else {
                email.append(em.charAt(i));
            }
        }
        String gmail=email.toString();
        user.setEmail(gmail);
        user.setPastRooms(new ArrayList<String>());
        user.setSocketId(null);
    System.out.println(user.getName());
        return userservice.newUser(user);
    }
    @PostMapping("/verifyToken")

    public ResponseEntity<User> verifyToken(@RequestBody  verToken entity)
    {
        String token = entity.token;
        System.out.println(entity);
        System.out.println(token);
        Optional<User> user= userservice.getUserByToken(token);
        if(user.isPresent())
        {

            User res=user.get();
            User u = new User("",res.getEmail(),"", res.getName(), res.getPhoto(),"",null);
//            return new HttpEntity<User>(u);
            return ResponseEntity.status(HttpStatus.OK).body(u);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new User());
        }

    }
    @PostMapping("/room/getDetails")
    public ArrayList<String> rooms(@RequestBody  verToken verToken)
    {
        String token = verToken.token;
        Optional<User> user= userservice.getUserByToken(token);
        if(user.isPresent())
        {

            User res=user.get();


            return res.getPastRooms();
        }
        else
        {
            return null;
        }
    }

    @PostMapping("/room/join")
    public ResponseEntity<JoinR> joinRoom (@RequestBody JoinR join)

    {
        int k2=0;
        String token= join.token;

        String roomCode= join.roomCode;
        int k= roomservice.validRoomCode(roomCode);
        Optional<User> user= userservice.getUserByToken(token);
        if(user.isPresent())
        {


           k2=1;


        }

        if (k==1 && k2==1)
        {
            System.out.println("hello");
            User u=user.get();
            ArrayList<String> a;
            if(u.getPastRooms()==null)
            {
                a=new ArrayList<String>();
            }
            else {
                a = u.getPastRooms();
            }
            if(!a.contains(roomCode)) {

                a.add(roomCode);
                u.setPastRooms(a);
                User u2 = userservice.updateUser(u);
            }
            Map<String,Cursors> members;
            Cursors cursors = new Cursors();
            cursors.python="0:0";
            cursors.javascript="0:0";
            cursors.cpp="0:0";
            Optional<Room> o=roomservice.findRoom(roomCode);
            if (o.isPresent())
            {
                Room r=o.get();
                members=r.getMembers();
                String email=u.getEmail();
                members.put(email,cursors);
                r.setMembers(members);
                Room room =roomservice.updateRoom(r);
            }
            return ResponseEntity.status(HttpStatus.OK).body(join);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JoinR());
        }

    }
    @PostMapping("/room/create")
    public ResponseEntity<Room> createRoom(@RequestBody CreateR create)
    {
        String token=create.token;
        String roomName=create.roomName;
        String defaultLanguage=create.defaultLanguage;
        Optional<User> user= userservice.getUserByToken(token);
        int k2=0;
        if(user.isPresent())
        {
            User u=user.get();
            String email=u.getEmail();
           Room r= roomservice.newRoom(roomName,defaultLanguage,email);
            ArrayList<String> a;
           if(u.getPastRooms()==null)
           {
               a=new ArrayList<String>();
           }
           else {
               a = u.getPastRooms();
           }

           if (r!=null) {
               a.add(r.getRoomCode());

               u.setPastRooms(a);

               User u2=userservice.updateUser(u);

               Room tosend = new Room("", r.getRoomCode(), "", null, "", null);
               return ResponseEntity.status(HttpStatus.OK).body(tosend);
           }
           else {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
           }

        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


    }



}
