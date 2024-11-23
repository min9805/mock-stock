package com.mock.investment.stock.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @Schema(description = "코인 심볼", example = "BTCUSDT")
    private String symbol;

    @Schema(description = "계좌 번호", example = "1234-5678")
    private String accountNumber;
}