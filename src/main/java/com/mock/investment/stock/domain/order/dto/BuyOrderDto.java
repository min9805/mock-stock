package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.domain.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuyOrderDto {
    @Schema(description = "주문 타입", example = "LIMIT")
    private OrderType orderType;

    @Schema(description = "주문 상태", example = "PENDING")
    private OrderStatus orderStatus;

    @Schema(description = "주문 가격", example = "10000")
    private Double buyPrice;

    @Schema(description = "주문 수량", example = "10")
    private Integer buyQuantity;

    @Schema(description = "매수 체결 수량", example = "3")
    private Integer filledQuantity;

    @Schema(description = "매수 주문 남은 수량", example = "7")
    private Integer remainingQuantity;

    public static BuyOrderDto fromEntity(BuyOrder buyOrder) {
        return BuyOrderDto.builder()
                .orderType(buyOrder.getOrderType())
                .orderStatus(buyOrder.getOrderStatus())
                .buyPrice(buyOrder.getBuyPrice())
                .buyQuantity(buyOrder.getBuyQuantity())
                .filledQuantity(buyOrder.getFilledQuantity())
                .remainingQuantity(buyOrder.getRemainingQuantity())
                .build();
    }
}
