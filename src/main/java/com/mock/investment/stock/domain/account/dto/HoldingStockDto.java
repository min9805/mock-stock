package com.mock.investment.stock.domain.account.dto;

import com.mock.investment.stock.domain.account.domain.HoldingStock;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class HoldingStockDto {
    @NotBlank
    private String symbol;

    @PositiveOrZero
    private BigDecimal quantity;

    @PositiveOrZero
    private Double avgPrice;

    public static List<HoldingStockDto> fromEntities(List<HoldingStock> holdingStocks) {
        return holdingStocks.stream()
                .map(HoldingStockDto::fromEntity)
                .toList();
    }

    public static HoldingStockDto fromEntity(HoldingStock holdingStock){
        return HoldingStockDto.builder()
                .symbol(holdingStock.getStock().getSymbol())
                .quantity(holdingStock.getQuantity())
                .avgPrice(holdingStock.getAvgPrice())
                .build();
    }
}
