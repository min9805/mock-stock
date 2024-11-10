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
    public static final BigDecimal DEFAULT_FEE_RATE = new BigDecimal("0.00015");

    @Comment("매도 주문 수수료")
    private BigDecimal fee;

    public SellOrder createModifiedOrder(SellOrder sellOrder, BigDecimal price) {
        return SellOrder.builder()
                .stock(sellOrder.getStock())
                .account(sellOrder.getAccount())
                .orderType(sellOrder.getOrderType())
                .price(price)
                .quantity(sellOrder.getRemainingQuantity())
                .orderStatus(OrderStatus.PENDING)
                .filledQuantity(BigDecimal.valueOf(0.0))
                .remainingQuantity(sellOrder.getRemainingQuantity())
                .fee(sellOrder.getFee())
                .build();
    }

    public BigDecimal calculateFee() {
        return DEFAULT_FEE_RATE.multiply(this.getPrice()).multiply(this.getFilledQuantity());
    }
}
