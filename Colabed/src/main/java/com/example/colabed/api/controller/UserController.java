package com.example.colabed.api.controller;

import com.example.colabed.api.model.User;
import com.example.colabed.api.model.Userrepository;
import com.example.colabed.config.WebSocketConfig;
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
import java.util.Optional;

@RestController
@CrossOrigin
public class UserController
{
    private Userservice userservice;
    @Autowired

    public UserController(Userservice userservice)
    {
        this.userservice=userservice;
    }
    @GetMapping("/")
    public String getUser()
    {

        return "Code Sync Server";
    }


    @PostMapping("/user")
    @ResponseStatus(value=HttpStatus.OK)
    public void Loginuser()throws Exception{}

    @PostMapping("/login")
    @ResponseStatus(value= HttpStatus.OK)
    public  User createUser(@RequestBody User user)
    {
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
            User u = new User("",res.getEmail(),"", res.getName(), res.getPhoto());
//            return new HttpEntity<User>(u);
            return ResponseEntity.status(HttpStatus.OK).body(u);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new User());
        }

    }


}
