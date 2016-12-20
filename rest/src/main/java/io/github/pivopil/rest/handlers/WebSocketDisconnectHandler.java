package io.github.pivopil.rest.handlers;

import io.github.pivopil.rest.constants.WS_API;
import io.github.pivopil.share.entities.ActiveWebSocketUser;
import io.github.pivopil.share.persistence.ActiveWebSocketUserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Arrays;

public class WebSocketDisconnectHandler<S>
        implements ApplicationListener<SessionDisconnectEvent> {
    private ActiveWebSocketUserRepository repository;
    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate,
                                      ActiveWebSocketUserRepository repository) {
        super();
        this.messagingTemplate = messagingTemplate;
        this.repository = repository;
    }

    public void onApplicationEvent(SessionDisconnectEvent event) {
        String id = event.getSessionId();
        if (id == null) {
            return;
        }
        ActiveWebSocketUser user = this.repository.findOne(id);
        if (user == null) {
            return;
        }

        this.repository.delete(id);
        this.messagingTemplate.convertAndSend(WS_API.TOPIC_FRIENDS_SIGNOUT,
                Arrays.asList(user.getUsername()));

    }
}
