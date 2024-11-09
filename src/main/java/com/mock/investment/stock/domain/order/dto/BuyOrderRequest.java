package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuyOrderRequest {
    @NotBlank
    @Schema(description = "주식 코드", example = "BTCUSDT")
    private String symbol;

    @NotBlank
    @Schema(description = "계좌 번호", example = "1234-1234-12")
    private String accountNumber;

    @Positive
    @Schema(description = "주문 수량", example = "10")
    private Double quantity;

    @Schema(description = "주문 가격", example = "10000")
    private Double price;

    @Schema(description = "주문 타입", example = "LIMIT")
    private OrderType orderType;
}
