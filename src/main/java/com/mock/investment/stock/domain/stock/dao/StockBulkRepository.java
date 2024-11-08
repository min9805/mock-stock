package com.mock.investment.stock.domain.stock.dao;

import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.stock.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StockBulkRepository {
	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public List<StockDto> saveAll(List<Stock> stockList) {
		String sql = "INSERT INTO stocks (symbol, base_coin, quote_coin) VALUES (?, ?, ?)";

		jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Stock stock = stockList.get(i);
						ps.setString(1, stock.getSymbol());
						ps.setString(2, stock.getBaseCoin());
						ps.setString(3, stock.getQuoteCoin());
					}

					@Override
					public int getBatchSize() {
						return stockList.size();
					}
				}
		);

		// ID 값을 가져오는 쿼리
		String selectSql = "SELECT id, symbol, base_coin, quote_coin FROM stocks WHERE symbol IN (:symbols)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("symbols", stockList.stream()
				.map(Stock::getSymbol)
				.collect(Collectors.toList()));

		List<Stock> savedStocks = namedParameterJdbcTemplate.query(
				selectSql,
				parameters,
				(rs, rowNum) -> Stock.builder()
						.id(rs.getLong("id"))
						.symbol(rs.getString("symbol"))
						.baseCoin(rs.getString("base_coin"))
						.quoteCoin(rs.getString("quote_coin"))
						.build()
		);

		return StockDto.fromEntities(savedStocks);
	}
}