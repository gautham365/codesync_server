package com.example.colabed.api.controller;

import com.example.colabed.api.model.JoinR;
import com.example.colabed.api.model.codeUpd;
import com.example.colabed.api.model.curUpd;
import com.example.colabed.service.Roomservice;
import com.example.colabed.service.Userservice;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.example.colabed.api.model.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Log4j2
@Component


public class SocketIOController {
    @Autowired
    private SocketIOServer socketServer;

    private Userservice userservice;
    private Roomservice roomservice;
    @Autowired

    public SocketIOController(Userservice userservice,Roomservice roomservice) {

        this.userservice=userservice;
        this.roomservice=roomservice;

    }

    SocketIOController(SocketIOServer socketServer)
    {
        this.socketServer=socketServer;
//        this.socketServer.addConnectListener(onUserConnectWithSocket);
//        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);
//        this.socketServer.addEventListener("joinRoom", JoinR.class, onJoinRoom);
//        this.socketServer.addEventListener("cursorUpdate", curUpd.class, onCursorUpdate);
//        this.socketServer.addEventListener("codeUpdate", codeUpd.class, onCodeUpdate);
    }

    public DataListener<JoinR> onJoinRoom = (client, message, acknowledge) -> {
        log.debug("rrrr"+message.getRoomCode()+"r");
//            socketServer.getBroadcastOperations().sendEvent(message.getTo(),client, message);
//            acknowledge.sendAckData("Message send to target user successfully");
    };
//    public DataListener<curUpd> onCursorUpdate = (client, message, acknowledge) -> {
//        log.info(message);
//            socketServer.getBroadcastOperations().sendEvent(message.getTo(),client, message);
//            acknowledge.sendAckData("Message send to target user successfully");
//    }; public DataListener<codeUpd> onCodeUpdate = (client, message, acknowledge) -> {
//        log.info(message);
//            socketServer.getBroadcastOperations().sendEvent(message.getTo(),client, message);
//            acknowledge.sendAckData("Message send to target user successfully");
//    };


}
