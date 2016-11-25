package io.github.pivopil.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig
        extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages

                .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.HEARTBEAT, SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT).permitAll()
                // matches any destination that starts with /rooms/
                .simpDestMatchers("/queue/**").authenticated()
                .simpDestMatchers("/topic/**").authenticated()
                // (i.e. cannot send messages directly to /topic/, /queue/)
                // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
                // /topic/messages-user<id>)
                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).authenticated()
                // catch all
                .anyMessage().authenticated();

        // https://github.com/jhipster/generator-jhipster/issues/1370
        //.simpMessageDestMatchers("/queue/**", "/topic/**").denyAll()
        //.simpSubscribeDestMatchers("/queue/**/*-user*", "/topic/**/*-user*").denyAll()
        //.anyMessage().authenticated();

    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
