package com.mock.investment.stock.domain.stock.dao;

import com.mock.investment.stock.domain.stock.dto.CandleRequest;
import com.mock.investment.stock.domain.stock.dto.CandleStick;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CandleRepository {
    private final MongoTemplate mongoTemplate;

    // 상수로 관리
    private static final String COLLECTION_PREFIX = "candle";
    private static final String COLLECTION_SEPARATOR = ":";

    public List<Document> findCandles(String symbol, int interval, char unit) {
        String collectionName = buildCollectionName(symbol, interval, unit);
        return mongoTemplate.findAll(Document.class, collectionName);
    }

    public List<Document> findRecentCandles(
            String symbol,
            int interval,
            char unit,
            int limit) {

        String collectionName = buildCollectionName(symbol, interval, unit);

        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "t"))
                .limit(limit);

        return mongoTemplate.find(query, Document.class, collectionName);
    }

    // 컬렉션 이름 생성 유틸리티
    private String buildCollectionName(String symbol, int interval, char unit) {
        return String.format("%s%s%s%s%d%s",
                COLLECTION_PREFIX,
                COLLECTION_SEPARATOR,
                symbol,
                COLLECTION_SEPARATOR,
                interval,
                unit
        );
    }

    public List<CandleStick> findCandlesBeforeEndTime(String symbol, int interval, Character unit, long endTime, int limit) {
        String collectionName = buildCollectionName(
                symbol,
                interval,
                unit
        );

        Query query = new Query(
                Criteria.where("startTime").lte(endTime)
        );

        query.with(Sort.by(Sort.Direction.DESC, "startTime"))
                .limit(limit);

        return mongoTemplate.find(query, Document.class, collectionName).stream()
                .map(this::parseCandleData)
                .toList();
    }

    private CandleStick parseCandleData(Document document) {
        return CandleStick.builder()
                .startTime(document.getLong("startTime"))
                .highPrice(document.getDouble("highPrice"))
                .lowPrice(document.getDouble("lowPrice"))
                .openPrice(document.getDouble("openPrice"))
                .closePrice(document.getDouble("closePrice"))
                .build();
    }
}