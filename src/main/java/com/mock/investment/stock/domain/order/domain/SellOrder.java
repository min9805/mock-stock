package com.mock.investment.stock.domain.order.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "sell_orders")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SellOrder extends Order {
    @Comment("매도 주문 수수료")
    private Double fee;
}
