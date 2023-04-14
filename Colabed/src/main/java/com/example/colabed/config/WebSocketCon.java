package com.example.colabed.config;

import com.example.colabed.api.model.Message;
import com.example.colabed.api.model.MessageDecoder;
import com.example.colabed.api.model.MessageEncoder;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/editor/{username}",decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
public class WebSocketCon  {
    private Session session;
    private static Set<WebSocketCon> Websockets
            = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session,@PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        Websockets.add(this);
        users.put(session.getId(), username);

        Message message = new Message();
        message.setFrom(username);
        message.setContent("Connected!");
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        message.setFrom(users.get(session.getId()));
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        Websockets.remove(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
    }
    @OnError
    public void onError(Session session, Throwable throwable) {
        // error handling here
    }
    private static void broadcast(Message message)
            throws IOException, EncodeException {

        Websockets.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}