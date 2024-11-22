package com.mock.investment.stock.domain.stock.domain;


import com.fasterxml.jackson.databind.JsonNode;
import com.mock.investment.stock.domain.stock.dto.TickerMessage;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Document
@Getter
@Builder
public class StockTick {
    @Id
    private ObjectId id;

    private String symbol;
    private Double lastPrice;

    // 시계열 쿼리를 위한 시간 필드들
    @Builder.Default
    private long timestamp = Instant.now().toEpochMilli();

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

    public static StockTick fromTickerMessage(TickerMessage tickerMessage) {
        LocalDateTime dateTime = Instant.ofEpochMilli(tickerMessage.getTs())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return StockTick.builder()
                .symbol(tickerMessage.getData().getSymbol())
                .lastPrice(tickerMessage.getData().getLastPrice().doubleValue())
                .volume24h(tickerMessage.getData().getVolume24h().doubleValue())
                .turnover24h(tickerMessage.getData().getTurnover24h().doubleValue())
                .highPrice24h(tickerMessage.getData().getHighPrice24h().doubleValue())
                .lowPrice24h(tickerMessage.getData().getLowPrice24h().doubleValue())
                .prevPrice24h(tickerMessage.getData().getPrevPrice24h().doubleValue())
                .price24hPcnt(tickerMessage.getData().getPrice24hPcnt().doubleValue())

                .timestamp(tickerMessage.getTs())
                .build();
    }
}
