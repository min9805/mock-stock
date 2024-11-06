package com.mock.investment.stock.domain.order.domain;

import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.user.domain.User;
import com.mock.investment.stock.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellOrderId;

    @PositiveOrZero
    @Comment("매도 주문 수량")
    private Integer sellQuantity;

    @Comment("매도 주문 가격")
    private Double sellPrice;

    @PositiveOrZero
    @Comment("매도 체결 수량")
    private Integer filledQuantity;

    @PositiveOrZero
    @Comment("매도 주문 남은 수량")
    private Integer remainingQuantity;

    @Comment("매도 주문 상태")
    private OrderStatus orderStatus;

    @Comment("매도 주문 타입")
    private OrderType orderType;

    @Comment("매도 주문 가격")
    private Double executedPrice;

    @Comment("매도 주문 수수료")
    private Double fee;

    @Comment("매도 주문 만료 시간")
    private LocalDateTime expireTime;

    @Comment("매도 주문 취소 시간")
    private LocalDateTime cancelledAt;

    @Comment("매도 주문 완료 시간")
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("매도 주문한 사용자")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    @Comment("매도 주문한 주식")
    private Stock stock;
}
