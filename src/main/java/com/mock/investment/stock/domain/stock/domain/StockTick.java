package com.mock.investment.stock.domain.stock.domain;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "stocks")
@Getter
@Builder
public class StockTick {
    @Id
    private ObjectId id;

    private String symbol;
    private Double lastPrice;

    // 시계열 쿼리를 위한 시간 필드들
    @Builder.Default
    private Instant timestamp = Instant.now();

    @Builder.Default
    private LocalDateTime datetime = LocalDateTime.now();

    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Builder.Default
    private int hour = LocalDateTime.now().getHour();

    @Builder.Default
    private int minute = LocalDateTime.now().getMinute();

    private Double volume24h;
    private Double turnover24h;
    private Double highPrice24h;
    private Double lowPrice24h;
    private Double prevPrice24h;
    private Double price24hPcnt;

    public static StockTick fromJson(JsonNode data) {
        return StockTick.builder()
                .symbol(data.get("symbol").asText())
                .lastPrice(data.get("lastPrice").asDouble())
                .volume24h(data.get("volume24h").asDouble())
                .turnover24h(data.get("turnover24h").asDouble())
                .highPrice24h(data.get("highPrice24h").asDouble())
                .lowPrice24h(data.get("lowPrice24h").asDouble())
                .prevPrice24h(data.get("prevPrice24h").asDouble())
                .price24hPcnt(data.get("price24hPcnt").asDouble())
                .build();
    }
}
