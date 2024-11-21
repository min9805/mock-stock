package com.mock.investment.stock.global.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.mock.investment.stock.domain.stock.domain.StockTick;
import com.mock.investment.stock.domain.stock.dto.StockTickDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@RequiredArgsConstructor
@Slf4j
public class StockInfoHolder {
    private final MongoTemplate mongoTemplate;
    private final ConcurrentHashMap<String, BigDecimal> currentPrices = new ConcurrentHashMap<>();
    private final RedissonClient redissonClient;

    public void updatePrice(JsonNode data) {
        String symbol = data.get("symbol").asText();
        BigDecimal price = new BigDecimal(data.get("lastPrice").asText());
        currentPrices.put(symbol, price);

        StockTick stockTick = StockTick.fromJson(data);

        RTopic topic = redissonClient.getTopic("stock/" + symbol);
        log.info(topic.toString());

        topic.publish(StockTickDto.fromEntity(stockTick));
        log.info("Published to Redis - Symbol: {}, Price: {}", symbol, price);

//        try {
//            mongoTemplate.save(stockTick);
//            log.debug("Saved to MongoDB - Symbol: {}, Price: {}", symbol, price);
//        } catch (Exception e) {
//            log.error("Failed to save to MongoDB - Symbol: {}", symbol, e);
//        }
    }

    public BigDecimal getCurrentPrice(String symbol) {
        return currentPrices.get(symbol);
    }
}