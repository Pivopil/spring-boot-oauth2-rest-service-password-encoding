package io.github.pivopil.rest.services.ws;

import io.github.pivopil.rest.constants.WS_API;
import io.github.pivopil.rest.models.InstantMessage;
import io.github.pivopil.rest.services.security.CustomSecurityService;
import io.github.pivopil.share.persistence.ActiveWebSocketUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 28.03.17.
 */
@Service
public class MessageService {

    private final CustomSecurityService customSecurityService;

    private final SimpMessageSendingOperations messagingTemplate;

    private final ActiveWebSocketUserRepository activeUserRepository;


    @Autowired
    public MessageService(CustomSecurityService customSecurityService, SimpMessageSendingOperations messagingTemplate, ActiveWebSocketUserRepository activeUserRepository) {
        this.customSecurityService = customSecurityService;
        this.messagingTemplate = messagingTemplate;
        this.activeUserRepository = activeUserRepository;
    }

    public void im(InstantMessage instantMessage) {
        instantMessage.setFrom(customSecurityService.userLoginFromAuthentication());
        this.messagingTemplate.convertAndSendToUser(instantMessage.getTo(), WS_API.QUEUE_MESSAGES, instantMessage);
        this.messagingTemplate.convertAndSendToUser(instantMessage.getFrom(), WS_API.QUEUE_MESSAGES, instantMessage);
    }

    public List<String> getAllActiveUsers() {
        return this.activeUserRepository.findAllActiveUsers(customSecurityService.userLoginFromAuthentication());
    }
}
