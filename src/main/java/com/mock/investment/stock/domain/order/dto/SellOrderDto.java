package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.domain.OrderType;
import com.mock.investment.stock.domain.order.domain.SellOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellOrderDto {
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

    public static SellOrderDto fromEntity(SellOrder sellOrder) {
        return SellOrderDto.builder()
                .orderType(sellOrder.getOrderType())
                .orderStatus(sellOrder.getOrderStatus())
                .buyPrice(sellOrder.getPrice())
                .buyQuantity(sellOrder.getQuantity())
                .filledQuantity(sellOrder.getFilledQuantity())
                .remainingQuantity(sellOrder.getRemainingQuantity())
                .build();
    }
}
