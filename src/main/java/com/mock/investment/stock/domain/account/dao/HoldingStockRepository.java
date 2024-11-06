package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.HoldingStocks;
import com.mock.investment.stock.domain.account.domain.WatchStocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoldingStockRepository extends JpaRepository<HoldingStocks, Long> {
    List<HoldingStocks> findByUserId(Long userId);
}
