package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellOrderRequest extends OrderRequest{
    @Positive
    @Schema(description = "주문 수량", example = "10")
    private Double quantity;

    @Schema(description = "주문 가격", example = "10000")
    private Double price;

    @Schema(description = "주문 타입", example = "LIMIT")
    private OrderType orderType;
}