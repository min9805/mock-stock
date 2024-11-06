package com.mock.investment.stock.domain.account.domain;

import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.user.domain.User;
import com.mock.investment.stock.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "holding_stocks")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoldingStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holding_stock_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    private Stock stock;

    private Long quantity;

    private Double avgPrice;
}
