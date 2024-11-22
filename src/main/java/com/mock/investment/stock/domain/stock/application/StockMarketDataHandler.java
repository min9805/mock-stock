package com.mock.investment.stock.domain.stock.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.investment.stock.domain.stock.domain.StockTick;
import com.mock.investment.stock.domain.stock.dto.CandleStick;
import com.mock.investment.stock.domain.stock.dto.TickerMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockMarketDataHandler {
    private final MongoTemplate mongoTemplate;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final List<Integer> intervalMinutes = Arrays.asList(1, 3, 5, 15, 30);
    private final List<Integer> intervalHours = Arrays.asList(1, 4, 12);
    private final List<Integer> intervalDays = Arrays.asList(1, 7);
    private final List<Integer> intervalMonths = List.of(1);
    private final List<Integer> intervalYears = List.of(1);

    private final List<List<Integer>> intervals = List.of(intervalMinutes, intervalHours, intervalDays, intervalMonths, intervalYears);
    private final List<Character> units = List.of('m', 'h', 'd', 'M', 'Y');

    private final String CANDLESTICK_KEY = "candle";

    private String formatKey(String symbol, int interval, char unit) {
        return String.format("%s:%s:%d%c", CANDLESTICK_KEY, symbol, interval, unit);
    }

    public void saveStockTickData(TickerMessage tickerMessage) {
        StockTick stockTick = StockTick.fromTickerMessage(tickerMessage);

        for (int i = 0; i < intervals.size(); i++) {
            for (int interval : intervals.get(i)) {
                updateTickDataAtRedis(stockTick, interval, units.get(i));
            }
        }
    }

    private void updateTickDataAtRedis(StockTick stockTick, int interval, char unit) {
        String key = formatKey(stockTick.getSymbol(), interval, unit);
        RDeque<String> deque = redissonClient.getDeque(key);

        try {
            String candleJson = deque.peekFirst();
            if (candleJson == null) {
                CandleStick candle = CandleStick.createCandle(stockTick);
                deque.add(objectMapper.writeValueAsString(candle));
                simpMessagingTemplate.convertAndSend("/topic/candle/" + key, candle);
            } else {
                CandleStick candle = objectMapper.readValue(candleJson, CandleStick.class);
                if(isSameInterval(candle, stockTick, interval, unit)) {
                    candle.updateCandle(stockTick);
                    deque.removeFirst();
                    deque.addFirst(objectMapper.writeValueAsString(candle));
                    simpMessagingTemplate.convertAndSend("/topic/candle/" + key, candle);
                } else {
                    CandleStick newCandle = CandleStick.createCandle(stockTick);
                    deque.addFirst(objectMapper.writeValueAsString(newCandle));
                    simpMessagingTemplate.convertAndSend("/topic/candle/" + key, newCandle);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing candle data for symbol: {}", e);
        }
    }

    private boolean isSameInterval(CandleStick candle, StockTick stockTick, int interval, char unit) {
        if (unit == 'm'){
            long minute1 = stockTick.getTimestamp() / 60000;
            long minute2 = candle.getStartTime() / 60000;
            return minute1 - minute2 < interval;
        } else if (unit == 'h') {
            long hour1 = stockTick.getTimestamp() / 3600000;
            long hour2 = candle.getStartTime() / 3600000;
            return hour1 - hour2 < interval;
        } else if (unit == 'd') {
            long day1 = stockTick.getTimestamp() / 86400000;
            long day2 = candle.getStartTime() / 86400000;
            return day1 - day2 < interval;
        } else if (unit == 'M') {
            LocalDateTime time1 = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(stockTick.getTimestamp()),
                    ZoneId.systemDefault());
            LocalDateTime time2 = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(candle.getStartTime()),
                    ZoneId.systemDefault());

            long monthsDiff = ChronoUnit.MONTHS.between(time2, time1);
            return monthsDiff < interval;
        } else if (unit == 'Y') {
            LocalDateTime time1 = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(stockTick.getTimestamp()),
                    ZoneId.systemDefault());
            LocalDateTime time2 = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(candle.getStartTime()),
                    ZoneId.systemDefault());

            long yearsDiff = ChronoUnit.YEARS.between(time2, time1);
            return yearsDiff < interval;
        }
        return false;
    }
}
