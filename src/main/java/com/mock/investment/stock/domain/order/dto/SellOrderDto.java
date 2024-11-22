package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.SellOrder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SellOrderDto extends OrderDto{
    public static SellOrderDto fromEntity(SellOrder sellOrder) {
        return SellOrderDto.builder()
                .orderType(sellOrder.getOrderType())
                .orderStatus(sellOrder.getOrderStatus())
                .price(sellOrder.getPrice())
                .quantity(sellOrder.getQuantity())
                .filledQuantity(sellOrder.getFilledQuantity())
                .remainingQuantity(sellOrder.getRemainingQuantity())
                .build();
    }
}
