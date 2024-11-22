package com.mock.investment.stock.global.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();

		switch (Objects.requireNonNull(accessor.getCommand())) {
			case CONNECT -> log.info("CONNECT: " + message);
			case CONNECTED -> log.info("CONNECTED: " + message);
			case DISCONNECT -> log.info("DISCONNECT: " + message);
			case SUBSCRIBE -> log.info("SUBSCRIBE: " + message);
			case UNSUBSCRIBE -> log.info("UNSUBSCRIBE: " + sessionId);
			case SEND -> log.info("SEND: " + sessionId);
			case MESSAGE -> log.info("MESSAGE: " + sessionId);
			case ERROR -> log.info("ERROR: " + sessionId);
			default -> log.info("UNKNOWN: " + sessionId);
		}

	}
}