package com.example.colabed.api.controller;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.colabed.api.model.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


@Log4j2
@Component


public class SocketIOController {
    @Autowired
    private SocketIOServer socketServer;



    SocketIOController(SocketIOServer socketServer)
    {
        this.socketServer=socketServer;
        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);
        this.socketServer.addEventListener("messageSendToUser", Message.class, onSendMessage);
    }
    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
            log.info("User Ready");
        }
    };
    public DisconnectListener onUserDisconnectWithSocket= new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
            log.info("User disconnected ");
        }
    };
    public DataListener<Message> onSendMessage= new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient client, Message message, AckRequest acknowledge) throws Exception {
            log.info(message.getFrom()+" user send message to user "+message.getTo()+" and message is "+message.getContent());
            socketServer.getBroadcastOperations().sendEvent(message.getTo(),client, message);
            acknowledge.sendAckData("Message send to target user successfully");
        }
    };
}
