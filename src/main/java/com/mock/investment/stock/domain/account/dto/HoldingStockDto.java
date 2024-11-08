package com.mock.investment.stock.domain.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HoldingStockDto {
    private String accountNumber;

    @NotBlank
    private String coinSymbol;

    @PositiveOrZero
    private int quantity;

    @PositiveOrZero
    private double avgPrice;

    @NotBlank
    private String koreanName;

    @NotBlank
    private String englishName;
}
