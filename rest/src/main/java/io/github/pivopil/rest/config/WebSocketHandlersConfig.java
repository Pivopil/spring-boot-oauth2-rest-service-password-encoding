package io.github.pivopil.rest.config;

import io.github.pivopil.rest.handlers.WebSocketConnectHandler;
import io.github.pivopil.rest.handlers.WebSocketDisconnectHandler;
import io.github.pivopil.rest.models.ActiveWebSocketUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.ExpiringSession;

@Configuration
public class WebSocketHandlersConfig<S extends ExpiringSession> {

	@Bean
	public WebSocketConnectHandler<S> webSocketConnectHandler(
			SimpMessageSendingOperations messagingTemplate,
			ActiveWebSocketUserRepository repository) {
		return new WebSocketConnectHandler<S>(messagingTemplate, repository);
	}

	@Bean
	public WebSocketDisconnectHandler<S> webSocketDisconnectHandler(
			SimpMessageSendingOperations messagingTemplate,
			ActiveWebSocketUserRepository repository) {
		return new WebSocketDisconnectHandler<S>(messagingTemplate, repository);
	}
}
