package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.domain.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BuyOrderDto extends OrderDto{
    public static BuyOrderDto fromEntity(BuyOrder buyOrder) {
        return BuyOrderDto.builder()
                .orderType(buyOrder.getOrderType())
                .orderStatus(buyOrder.getOrderStatus())
                .buyPrice(buyOrder.getPrice())
                .buyQuantity(buyOrder.getQuantity())
                .filledQuantity(buyOrder.getFilledQuantity())
                .remainingQuantity(buyOrder.getRemainingQuantity())
                .build();
    }
}
