package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.BuyOrder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BuyOrderDto extends OrderDto{
    public static BuyOrderDto fromEntity(BuyOrder buyOrder) {
        return BuyOrderDto.builder()
                .orderType(buyOrder.getOrderType())
                .orderStatus(buyOrder.getOrderStatus())
                .price(buyOrder.getPrice())
                .quantity(buyOrder.getQuantity())
                .filledQuantity(buyOrder.getFilledQuantity())
                .remainingQuantity(buyOrder.getRemainingQuantity())
                .build();
    }
}
