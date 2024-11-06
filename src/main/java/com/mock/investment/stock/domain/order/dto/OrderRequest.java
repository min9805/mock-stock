package com.mock.investment.stock.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderRequest {
    @Schema(description = "주식 코드", example = "KRW-BIT")
    private String stockCode;

    @Schema(description = "계좌 번호", example = "1234-5678")
    private String accountNumber;
}
