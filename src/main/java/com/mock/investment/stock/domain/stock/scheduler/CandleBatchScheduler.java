package com.mock.investment.stock.domain.stock.scheduler;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CandleBatchScheduler {
    private final RedissonClient redissonClient;
    private final MongoTemplate mongoTemplate;

    private final String CANDLE_KEY = "candle";
    private final List<String> STOCKS = List.of("BTCUSDT");


    @Scheduled(cron = "0 */15 * * * *") // 매시 0, 15, 30, 45분에 실행
    public void batchCandleData() {
        for (String stock : STOCKS) {
            RDeque<Object> deque = redissonClient.getDeque(CANDLE_KEY + ":" + stock + ":1m");
            int dequeSize = deque.size();

            if (dequeSize > 1) {  // 데이터가 2개 이상일 때만 처리
                List<Object> candles = new ArrayList<>();
                // size-1 만큼만 데이터 가져오기
                for (int i = 0; i < dequeSize - 1; i++) {
                    Object candle = deque.pollLast();
                    if (candle != null) {
                        candles.add(candle);
                    }
                }

                // 벌크 저장
                List<Document> documents = candles.stream()
                        .map(candle -> Document.parse(candle.toString()))
                        .collect(Collectors.toList());

                mongoTemplate.insert(documents, CANDLE_KEY + ":" + stock + ":1m");
            }
        }
    }
}
