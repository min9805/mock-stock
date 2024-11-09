package com.mock.investment.stock.domain.account.dao;

import com.mock.investment.stock.domain.account.domain.HoldingStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingStockRepository extends JpaRepository<HoldingStock, Long> {
    List<HoldingStock> findByAccount_AccountNumber(String accountNumber);

    Optional<Object> findByAccount_AccountNumberAndStockSymbol(String accountNumber, String symbol);
}
