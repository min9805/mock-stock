package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.HoldingStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HoldingStockRepository extends JpaRepository<HoldingStock, Long> {
    Optional<HoldingStock> findFirstByAccount_AccountNumberAndStockSymbol(String accountNumber, String symbol);
}
