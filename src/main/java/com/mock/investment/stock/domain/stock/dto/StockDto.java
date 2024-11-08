package com.mock.investment.stock.domain.stock.dto;

import com.mock.investment.stock.domain.stock.domain.Stock;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StockDto {
	private String symbol;
	private String baseCoin;
	private String quoteCoin;

	public static StockDto fromEntity(Stock stock) {
		return StockDto.builder()
				.symbol(stock.getSymbol())
				.baseCoin(stock.getBaseCoin())
				.quoteCoin(stock.getQuoteCoin())
				.build();
	}

	public static List<StockDto> fromEntities(List<Stock> stockList) {
		return stockList.stream()
				.map(StockDto::fromEntity)
				.toList();
	}
}