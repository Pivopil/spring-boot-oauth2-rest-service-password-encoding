package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.constants.WS_API;
import io.github.pivopil.rest.models.InstantMessage;
import io.github.pivopil.rest.services.ws.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping(WS_API.INSTANT_MESSAGE)
    public void im(InstantMessage im) {
        messageService.im(im);
    }

    @SubscribeMapping(WS_API.ACTIVE_USERS)
    public List<String> subscribeMessages() {
        return messageService.getAllActiveUsers();
    }
}
