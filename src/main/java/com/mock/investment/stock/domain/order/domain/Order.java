package com.mock.investment.stock.domain.order.domain;

import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.order.exception.InvalidOrderToCancelException;
import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    @Comment("주문 수량")
    private Double quantity;

    @Comment("주문 상태")
    private OrderStatus orderStatus;

    @Comment("주문 타입")
    private OrderType orderType;

    @Comment("주문 가격")
    private Double price;

    @PositiveOrZero
    @Comment("체결 수량")
    private Double filledQuantity;

    @PositiveOrZero
    @Comment("주문 남은 수량")
    private Double remainingQuantity;

    @Comment("체결 가격")
    private Double executedPrice;

    @Comment("주문 만료 시간")
    private LocalDateTime expireAt;

    @Comment("주문 취소 시간")
    private LocalDateTime cancelledAt;

    @Comment("주문 완료 시간")
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @Comment("주문한 계좌")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    @Comment("주문한 주식")
    private Stock stock;

    public void cancel() {
        if (this.orderStatus == OrderStatus.PENDING || this.orderStatus == OrderStatus.PARTIALLY_FILLED) {
            this.orderStatus = OrderStatus.CANCELLED;
            this.cancelledAt = LocalDateTime.now();
        } else {
            throw new InvalidOrderToCancelException(this.id);
        }
    }
}
