package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.handlers.CurrentUser;
import io.github.pivopil.rest.models.ActiveWebSocketUserRepository;
import io.github.pivopil.rest.models.InstantMessage;
import io.github.pivopil.share.entities.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageController {
    private SimpMessageSendingOperations messagingTemplate;
    private ActiveWebSocketUserRepository activeUserRepository;

    @Autowired
    public MessageController(ActiveWebSocketUserRepository activeUserRepository,
                             SimpMessageSendingOperations messagingTemplate) {
        this.activeUserRepository = activeUserRepository;
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/im")
    public void im(InstantMessage im, @CurrentUser User currentUser) {
        im.setFrom(currentUser.getLogin());
        this.messagingTemplate.convertAndSendToUser(im.getTo(), "/queue/messages", im);
        this.messagingTemplate.convertAndSendToUser(im.getFrom(), "/queue/messages", im);
    }

    @SubscribeMapping("/users")
    public List<String> subscribeMessages(@CurrentUser User currentUser) throws Exception {
        return this.activeUserRepository.findAllActiveUsers(currentUser.getName());
    }
}
