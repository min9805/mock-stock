package com.mock.investment.stock.domain.stock.dto;

import com.mock.investment.stock.domain.stock.domain.StockTick;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockTickDto {
    private String symbol;
    private Double lastPrice;

    private Double volume24h;
    private Double turnover24h;
    private Double highPrice24h;
    private Double lowPrice24h;
    private Double prevPrice24h;
    private Double price24hPcnt;

    public static StockTickDto fromEntity(StockTick stockTick) {
        return StockTickDto.builder()
                .symbol(stockTick.getSymbol())
                .lastPrice(stockTick.getLastPrice())
                .volume24h(stockTick.getVolume24h())
                .turnover24h(stockTick.getTurnover24h())
                .highPrice24h(stockTick.getHighPrice24h())
                .lowPrice24h(stockTick.getLowPrice24h())
                .prevPrice24h(stockTick.getPrevPrice24h())
                .price24hPcnt(stockTick.getPrice24hPcnt())
                .build();
    }
}
