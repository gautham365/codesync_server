package com.example.colabed.api.controller;
import com.example.colabed.config.WebSocketCon;
import javax.annotation.PostConstruct;
import org.glassfish.tyrus.server.Server;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.DeploymentException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
@Component
public class SocketRun {


//    public SocketRun()
//    {
//        runServer();
//    }

    @PostConstruct
    private static void runServer() {
        Server server = null;
        System.out.println("here");
        try {
            server = new Server("localhost", 8081, "/editor", null, WebSocketCon.class);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            server.start();
            System.out.println("server started...");
//            System.out.println("Please press a key to stop the server.");
//            reader.readLine();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (DeploymentException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (server != null) {
//                server.stop();
//            }
//        }

        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }
    }
}
