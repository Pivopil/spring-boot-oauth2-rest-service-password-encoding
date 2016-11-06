package io.github.pivopil.rest.config;

import io.github.pivopil.REST_API;
import io.github.pivopil.rest.handlers.CustomWebSocketHandler;
import io.github.pivopil.rest.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created on 25.10.16.
 */

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(echoHandler(), REST_API.WS);
    }

    @Bean
    public CustomWebSocketHandler echoHandler() {
        return new CustomWebSocketHandler(webSocketService);
    }
}
