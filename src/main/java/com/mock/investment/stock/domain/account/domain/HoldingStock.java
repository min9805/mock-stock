package com.mock.investment.stock.domain.account.domain;

import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "holding_stocks")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoldingStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol")
    private Stock stock;

    private BigDecimal quantity;

    private Double avgPrice;

    public void addQuantity(BigDecimal amount, BigDecimal currentPrice) {
        this.quantity = this.quantity.add(amount);
        this.avgPrice = (this.avgPrice * this.quantity.doubleValue() + currentPrice.doubleValue() * amount.doubleValue()) / (this.quantity.doubleValue() + amount.doubleValue());
    }

    public void subtractQuantity(BigDecimal amount) {
        this.quantity = this.quantity.subtract(amount);
    }
}
