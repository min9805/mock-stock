package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.HoldingStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoldingStockRepository extends JpaRepository<HoldingStock, Long> {
    List<HoldingStock> findByUserId(Long userId);
}
