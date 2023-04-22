package com.example.colabed.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.colabed.api.model.JoinR;
import com.example.colabed.api.model.User;
import com.example.colabed.api.model.CodeUpd;
import com.example.colabed.api.model.CurUpd;
import com.example.colabed.service.Roomservice;
import com.example.colabed.service.Userservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

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
                System.out.println(user.get().getName() + " " +sId);
            }else{
                throw new Exception("Unauthorised");
            }
                client.sendEvent("authConnect","join");
            }catch (Exception e){
                log.error("Error"+e);
                client.disconnect();
            }
        });
        server.addDisconnectListener(client -> {
           String sId = String.valueOf(client.getSessionId());
           String token = client.get("token");
           String roomCode = client.get("roomCode");
           try{
               ObjectMapper mapper = new ObjectMapper();
               ObjectNode json = mapper.createObjectNode();
               json.put("email", "");
               json.put("name", "");
               json.put("lineNumber", 0);
               json.put("column", 0);
               json.put("type", "leave");

               server.getRoomOperations(roomCode).sendEvent("activeUsersUpdate",json);
               client.leaveRoom(roomCode);
               roomservice.removeUserFromRoom(client.get("token"),client.get("roomCode"));
               Optional<User> user = userservice.updateSocketId(token,"");
               if (user.isPresent()){
                   System.out.println("off"+user.get().getName());
               }else{
                   throw new Exception("Unauthorised");
               }
           }catch(Exception e){
               log.error("Error"+e);
           }
        });

        server.addEventListener("joinRoom", JoinR.class, (client, message, ackRequest) -> {
            try {
                log.debug(message.toString());
                client.set("roomCode", message.getRoomCode());
                client.joinRoom(message.getRoomCode());
                User u = roomservice.addUserToRoom(message.getToken(), message.getRoomCode());

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode json = mapper.createObjectNode();
                json.put("email", u.getEmail().replaceAll("\\?","."));
                json.put("name", u.getName());
                json.put("lineNumber", 0);
                json.put("column", 0);
                json.put("type", "join");

                server.getRoomOperations(message.getRoomCode()).sendEvent("activeUsersUpdate",json);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        });

        server.addEventListener("codeUpdate", CodeUpd.class, (client, message, ackRequest) -> {
            System.out.println(message.code);

        });

        server.addEventListener("cursorUpdate", CurUpd.class, (client, message, ackRequest) -> {
//            System.out.println(message.cursor);
            User u = roomservice.updateCursorOfUser(message.roomCode, message.language, message.cursor, client.get("token"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("email", u.getEmail());
        json.put("cursor", message.cursor);
//        System.out.println(server.getRoomOperations(message.roomCode).getClients());
//        server.getBroadcastOperations().sendEvent("cursorUpdate",json,message.roomCode);
        server.getRoomOperations(message.roomCode).sendEvent("cursorUpdate",json);
        });
        return server;
    }
    @PreDestroy
    public void stopSocketIOServer() {

        this.server.stop();
    }

}