package com.mock.investment.stock.domain.order.dto;

import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.domain.OrderStatus;
import com.mock.investment.stock.domain.order.domain.OrderType;
import com.mock.investment.stock.domain.order.domain.SellOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SellOrderDto extends OrderDto{
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
