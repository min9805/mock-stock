package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.WatchStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchStockRepository extends JpaRepository<WatchStock, Long> {
    List<WatchStock> findByUserId(Long userId);

    void deleteByUserIdAndStockSymbol(Long userId, String symbol);
}
