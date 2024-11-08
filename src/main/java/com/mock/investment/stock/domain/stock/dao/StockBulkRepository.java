package com.mock.investment.stock.domain.stock.dao;

import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockBulkRepository {
	private final JdbcTemplate jdbcTemplate;

	@Transactional
	public List<StockDto> saveAll(List<Stock> stockList) {
		String sql = "INSERT INTO stocks (symbol, base_coin, quote_coin) VALUES (?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, stockList, stockList.size(), (ps, stock) -> {
			ps.setString(1, stock.getSymbol());
			ps.setString(2, stock.getBaseCoin());
			ps.setString(3, stock.getQuoteCoin());
		});

		return StockDto.fromEntities(stockList);
	}
}