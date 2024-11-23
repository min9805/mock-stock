package com.mock.investment.stock.global.websocket;

import com.mock.investment.stock.domain.user.application.UserDetailsImpl;
import com.mock.investment.stock.domain.user.application.UserDetailsServiceImpl;
import com.mock.investment.stock.domain.user.dao.UserRepository;
import com.mock.investment.stock.domain.user.domain.User;
import com.mock.investment.stock.global.jwt.TokenService;
import com.mock.investment.stock.global.jwt.exception.InvalidJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {
	private final TokenService tokenService;
	private final UserRepository userRepository;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		String sessionId = accessor.getSessionId();

		if (Objects.requireNonNull(accessor.getCommand()) == StompCommand.SEND) {
			String tokenHeader = accessor.getFirstNativeHeader("Authorization");
			if (tokenHeader == null) {
				log.error("No token found in message from session: " + sessionId);
				throw new InvalidJwtException("No token found");
			}

			String token = tokenHeader.replace("Bearer ", "");
			String userEmail = tokenService.validateAccessToken(token);

			UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

			accessor.setUser(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

		}

		return message;
	}

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