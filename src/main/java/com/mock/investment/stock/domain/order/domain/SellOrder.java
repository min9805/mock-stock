package com.mock.investment.stock.domain.order.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Table(name = "sell_orders")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SellOrder extends Order {
    public static final Double DEFAULT_FEE_RATE = 0.001;

    @Comment("매도 주문 수수료")
    private Double fee;

    public SellOrder createModifiedOrder(SellOrder sellOrder, Double price) {
        return SellOrder.builder()
                .stock(sellOrder.getStock())
                .account(sellOrder.getAccount())
                .orderType(sellOrder.getOrderType())
                .price(price)
                .quantity(sellOrder.getRemainingQuantity())
                .orderStatus(OrderStatus.PENDING)
                .filledQuantity(0.0)
                .remainingQuantity(sellOrder.getRemainingQuantity())
                .fee(sellOrder.getFee())
                .build();
    }

    public Double calculateFee() {
        return super.getPrice() * super.getFilledQuantity() * DEFAULT_FEE_RATE;
    }
}
