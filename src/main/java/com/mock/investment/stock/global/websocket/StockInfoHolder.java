package com.mock.investment.stock.global.websocket;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StockInfoHolder {
    private ConcurrentHashMap<String, BigDecimal> currentPrices = new ConcurrentHashMap<>();

    public void updatePrice(String symbol, BigDecimal price) {
        currentPrices.put(symbol, price);
    }

    public BigDecimal getCurrentPrice(String symbol) {
        return currentPrices.get(symbol);
    }

    public ConcurrentHashMap<String, BigDecimal> getCurrentPrices() {
        return currentPrices;
    }
}
