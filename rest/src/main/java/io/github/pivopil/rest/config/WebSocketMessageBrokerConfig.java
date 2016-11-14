package io.github.pivopil.rest.config;

import io.github.pivopil.rest.handlers.CustomHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Created on 02.11.16.
 */
@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

    protected void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/messages")
                .addInterceptors(new CustomHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();
    }

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue/", "/topic/");
        registry.setApplicationDestinationPrefixes("/app");
    }

}
