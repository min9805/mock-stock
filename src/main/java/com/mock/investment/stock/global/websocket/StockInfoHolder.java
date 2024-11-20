package com.mock.investment.stock.global.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.mock.investment.stock.domain.stock.domain.StockTick;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    public void updatePrice(JsonNode data) {
        String symbol = data.get("symbol").asText();
        BigDecimal price = new BigDecimal(data.get("lastPrice").asText());
        currentPrices.put(symbol, price);

//        StockTick stockTick = StockTick.fromJson(data);
//
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
