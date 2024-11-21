package com.mock.investment.stock.domain.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerMessage {
    private String topic;
    private long ts;
    private String type;
    private long cs;
    private TickerData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TickerData {
        private String symbol;

        @JsonProperty("lastPrice")
        private BigDecimal lastPrice;

        @JsonProperty("highPrice24h")
        private BigDecimal highPrice24h;

        @JsonProperty("lowPrice24h")
        private BigDecimal lowPrice24h;

        @JsonProperty("prevPrice24h")
        private BigDecimal prevPrice24h;

        @JsonProperty("volume24h")
        private BigDecimal volume24h;

        @JsonProperty("turnover24h")
        private BigDecimal turnover24h;

        @JsonProperty("price24hPcnt")
        private BigDecimal price24hPcnt;

        @JsonProperty("usdIndexPrice")
        private BigDecimal usdIndexPrice;
    }
}