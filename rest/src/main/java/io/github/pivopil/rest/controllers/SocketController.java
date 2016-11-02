package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.models.Greeting;
import io.github.pivopil.rest.models.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created on 02.11.16.
 */
@Controller
public class SocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello, " + message.getName() + "!");
    }

}
