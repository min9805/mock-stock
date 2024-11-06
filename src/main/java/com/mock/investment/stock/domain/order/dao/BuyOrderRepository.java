package com.mock.investment.stock.domain.order.dao;

import com.mock.investment.stock.domain.order.domain.BuyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyOrderRepository extends JpaRepository<BuyOrder, Long>{
    List<BuyOrder> findByAccount_AccountNumberAndStockCode(String accountNumber, String stockCode);
}
