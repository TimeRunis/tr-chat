package com.tr.chat;

import com.tr.chat.websocket.WebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TrChatApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TrChatApplication.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        WebSocket.setApplicationContext(configurableApplicationContext);

    }

}
