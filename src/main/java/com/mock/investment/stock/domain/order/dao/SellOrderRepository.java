package com.mock.investment.stock.domain.order.dao;

import com.mock.investment.stock.domain.order.domain.BuyOrder;
import com.mock.investment.stock.domain.order.domain.SellOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellOrderRepository extends JpaRepository<SellOrder, Long>{
    List<SellOrder> findByAccount_AccountNumberAndStockCode(String accountNumber, String stockCode);
}
