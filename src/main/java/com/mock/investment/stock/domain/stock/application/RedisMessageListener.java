package com.mock.investment.stock.domain.stock.application;

import com.mock.investment.stock.domain.stock.dto.StockTickDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
@RequiredArgsConstructor
public class RedisMessageListener {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final RedissonClient redissonClient;

	@PostConstruct
	public void init() {
		RTopic topic = redissonClient.getTopic("stock/BTCUSDT");
		topic.addListener(StockTickDto.class, (channel, tickDto) -> {
			// Redis에서 받은 메시지를 STOMP 클라이언트들에게 전달
			simpMessagingTemplate.convertAndSend("/topic/stock/BTCUSDT", tickDto);
		});
	}
}