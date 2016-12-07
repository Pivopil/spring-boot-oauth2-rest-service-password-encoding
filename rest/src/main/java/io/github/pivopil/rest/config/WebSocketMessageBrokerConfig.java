package io.github.pivopil.rest.config;

import io.github.pivopil.rest.constants.WS_API;
import io.github.pivopil.rest.handlers.CustomHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created on 02.11.16.
 */
@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {


    protected void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WS_API.HANDSHAKE)
                .addInterceptors(new CustomHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();
    }

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(WS_API.QUEUE_DESTINATION_PREFIX, WS_API.TOPIC_DESTINATION_PREFIX);
        registry.setApplicationDestinationPrefixes(WS_API.APP_PREFIX);
    }

}
