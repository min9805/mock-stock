package com.mock.investment.stock.domain.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class HoldingStockDto {
    private String accountNumber;

    @NotBlank
    private String coinSymbol;

    @PositiveOrZero
    private int quantity;

    @PositiveOrZero
    private BigDecimal avgPrice;
}
