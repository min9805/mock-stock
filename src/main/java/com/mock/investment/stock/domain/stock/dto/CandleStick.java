package com.mock.investment.stock.domain.stock.dto;

import com.mock.investment.stock.domain.stock.domain.StockTick;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CandleStick {
    private long startTime;      // 시작 시간 (ms)
    private double openPrice;    // 시가
    private double highPrice;    // 고가
    private double lowPrice;     // 저가
    private double closePrice;   // 종가
    @Builder.Default
    private double volume = 0.0;       // 거래량
    @Builder.Default
    private double turnover = 0.0;     // 거래대금

    public static CandleStick createCandle(StockTick stockTick) {
        return CandleStick.builder()
                .startTime(stockTick.getTimestamp())
                .openPrice(stockTick.getLastPrice())
                .highPrice(stockTick.getLastPrice())
                .lowPrice(stockTick.getLastPrice())
                .closePrice(stockTick.getLastPrice())
                .build();
    }

    public void updateCandle(StockTick stockTick) {
        this.highPrice = Math.max(this.highPrice, stockTick.getLastPrice());
        this.lowPrice = Math.min(this.lowPrice, stockTick.getLastPrice());
        this.closePrice = stockTick.getLastPrice();
    }
}


