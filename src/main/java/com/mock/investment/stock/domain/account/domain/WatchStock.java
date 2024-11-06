package com.mock.investment.stock.domain.account.domain;

import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "watch_stocks")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WatchStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long watch_stock_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    private Stock stock;
}
