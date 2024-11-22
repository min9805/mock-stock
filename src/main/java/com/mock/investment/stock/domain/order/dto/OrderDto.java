package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.domain.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class OrderDto {
    @Schema(description = "주문 타입", example = "LIMIT")
    private OrderType orderType;

    @Schema(description = "주문 상태", example = "PENDING")
    private OrderStatus orderStatus;

    @Schema(description = "주문 가격", example = "10000")
    private BigDecimal price;

    @Schema(description = "주문 수량", example = "10")
    private BigDecimal quantity;

    @Schema(description = "체결 수량", example = "3")
    private BigDecimal filledQuantity;

    @Schema(description = "남은 수량", example = "7")
    private BigDecimal remainingQuantity;

}
