package com.example.colabed.config;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.example.colabed.api.model.JoinR;
import com.example.colabed.api.model.User;
import com.example.colabed.api.model.codeUpd;
import com.example.colabed.api.model.curUpd;
import com.example.colabed.service.Roomservice;
import com.example.colabed.service.Userservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.log4j.Log4j2;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PreDestroy;
import java.util.Optional;

@CrossOrigin
@Log4j2
@ComponentScan
@org.springframework.context.annotation.Configuration

public class WebSocketCon{
    private String SOCKETHOST="localhost";

    private int SOCKETPORT=8081;
    private SocketIOServer server;
    private Userservice userservice;
    private Roomservice roomservice;
    @Autowired

    public WebSocketCon(Userservice userservice,Roomservice roomservice) {

        this.userservice=userservice;
        this.roomservice=roomservice;

    }
    @Bean
    public SocketIOServer socketIOServer()
    {
        Configuration config= new Configuration();
        config.setHostname(SOCKETHOST);
        config.setPort(SOCKETPORT);
        server=new SocketIOServer(config);
        server.start();
        server.addConnectListener(client -> {
            try {
                String sId = String.valueOf(client.getSessionId());
                String token = client.getHandshakeData().getUrlParams().get("token").get(0);
                client.set("token",token);
            Optional<User> user = userservice.updateSocketId(token,sId);
            if (user.isPresent()){
                System.out.println(user.get().getName());
            }else{
                throw new Exception("Unauthorised");
            }
            }catch (Exception e){
                log.error("Error"+e);
                client.disconnect();
            }

        });
        server.addDisconnectListener(client -> {
           String sId = String.valueOf(client.getSessionId());
           String token = client.get("token");
           try{
               Optional<User> user = userservice.updateSocketId(token,sId);
               if (user.isPresent()){
                   System.out.println(user.get().getName());
               }else{
                   throw new Exception("Unauthorised");
               }
           }catch(Exception e){
               log.error("Error"+e);
           }
        });

        server.addEventListener("joinRoom", JoinR.class, (client, message, ackRequest) -> {
            System.out.println(client.getSessionId() + message.getRoomCode()+ message);
            client.joinRoom(message.getRoomCode());
        }); server.addEventListener("codeUpdate", codeUpd.class, (client, message, ackRequest) -> {
            System.out.println(message.code);

        }); server.addEventListener("cursorUpdate", curUpd.class, (client, message, ackRequest) -> {
            System.out.println(message.cursor);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("name", "John Doe");
        json.put("cursor", message.cursor);

        server.getRoomOperations(message.roomCode).sendEvent("cursorUpdate",json);
        });
        return server;
    }
    @PreDestroy
    public void stopSocketIOServer() {

        this.server.stop();
    }

}