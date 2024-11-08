package com.mock.investment.stock.domain.stock.dto;

import com.mock.investment.stock.domain.stock.domain.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StockCodeRequestFromBybit {
	private String symbol;

	private String baseCoin;

	private String quoteCoin;

	public Stock toEntity() {
		return Stock.builder()
				.symbol(symbol)
				.baseCoin(baseCoin)
				.quoteCoin(quoteCoin)
				.build();
	}
}