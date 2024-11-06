package com.mock.investment.stock.domain.order.domain;

import com.mock.investment.stock.domain.account.domain.Account;
import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "buy_orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuyOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyOrderId;

    @PositiveOrZero
    @Comment("매수 주문 수량")
    private Integer buyQuantity;

    @Comment("매수 주문 상태")
    private OrderStatus orderStatus;

    @Comment("매수 주문 타입")
    private OrderType orderType;

    @Comment("매수 주문 가격")
    private Double buyPrice;

    @PositiveOrZero
    @Comment("매수 체결 수량")
    private Integer filledQuantity;

    @PositiveOrZero
    @Comment("매수 주문 남은 수량")
    private Integer remainingQuantity;

    @Comment("매수 체결 가격")
    private Double executedPrice;

    @Comment("매수 주문 수수료")
    private Double fee;

    @Comment("매수 주문 만료 시간")
    private LocalDateTime expireAt;

    @Comment("매수 주문 취소 시간")
    private LocalDateTime cancelledAt;

    @Comment("매수 주문 완료 시간")
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @Comment("매수 주문한 계좌")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    @Comment("매수 주문한 주식")
    private Stock stock;
}
