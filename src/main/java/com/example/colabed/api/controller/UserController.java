package com.example.colabed.api.controller;

import com.example.colabed.api.model.*;
import com.example.colabed.service.Roomservice;
import com.example.colabed.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
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

    public ResponseEntity<User> verifyToken(@RequestBody VerToken entity)
    {
        String token = entity.token;
//        System.out.println(entity);
//        System.out.println(token);
        Optional<User> user= userservice.getUserByToken(token);
        if(user.isPresent())
        {

            User res=user.get();
            User u = new User("",res.getEmail().replaceAll("\\?","."),"", res.getName(), res.getPhoto(),"",null);
//            return new HttpEntity<User>(u);
            return ResponseEntity.status(HttpStatus.OK).body(u);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new User());
        }

    }
    @PostMapping("/room/getDetails")
    public ResponseEntity<ArrayList<PastRoom>> rooms(@RequestBody VerToken verToken) throws Exception {
        String token = verToken.token;
        Optional<User> user= userservice.getUserByToken(token);
        ArrayList<PastRoom> data;
        if(user.isPresent())
        {

            User res=user.get();
            try{
            data = roomservice.getPastRoomsWithRoomNames(token);
            }catch(Exception e){
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
            }
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/room/join")
    public ResponseEntity<JoinResp> joinRoom (@RequestBody JoinResp join) throws NoSuchFieldException, IllegalAccessException {
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
            if (o.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JoinResp());
            }
                Room r=o.get();
                members=r.getMembers();
                String email=u.getEmail();
                members.put(email,cursors);
                r.setMembers(members);
                Room room =roomservice.updateRoom(r);

                int i=1;
                ArrayList<ActiveUserData> data = new ArrayList<>();
                for (String activeUser: members.keySet()){
                    ActiveUserData aD = new ActiveUserData();

                    Field field = Cursors.class.getDeclaredField(r.getDefaultLanguage());
                    String[] parts = field.get(members.get(activeUser)).toString().split(":");

                    aD.id = String.valueOf(i++);
                    aD.lineNumber = Integer.parseInt(parts[0]);
                    aD.column = Integer.parseInt(parts[1]);
                    aD.email = activeUser.replaceAll("\\?",".");
                    aD.name = u.getName();
                    data.add(aD);
                }
                join.defaultLanguage=r.getDefaultLanguage();
                join.activeUsers=data;
                join.token="";
            return ResponseEntity.status(HttpStatus.OK).body(join);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JoinResp());
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
