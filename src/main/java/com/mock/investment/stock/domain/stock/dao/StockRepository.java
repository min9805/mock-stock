package com.mock.investment.stock.domain.stock.dao;

import com.mock.investment.stock.domain.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String> {
    Stock findBySymbol(String coinSymbol);
}