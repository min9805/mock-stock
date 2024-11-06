package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.WatchStocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchStockRepository extends JpaRepository<WatchStocks, Long> {
    List<WatchStocks> findByUserId(Long userId);

    void deleteByUserIdAndStockCode(Long userId, String stockCode);
}
