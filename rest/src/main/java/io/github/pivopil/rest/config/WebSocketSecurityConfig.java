/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pivopil.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * @author Rob Winch
 */
@Configuration
public class WebSocketSecurityConfig
		extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	// @formatter:off
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

			//.simpMessageDestMatchers("/queue/**", "/topic/**").denyAll()
			//.simpSubscribeDestMatchers("/queue/**/*-user*", "/topic/**/*-user*").denyAll()
			//.anyMessage().authenticated();

	}
	// @formatter:on

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}
}
