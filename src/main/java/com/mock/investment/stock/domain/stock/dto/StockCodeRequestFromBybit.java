package com.mock.investment.stock.domain.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mock.investment.stock.domain.stock.domain.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockCodeRequestFromBybit {
	@JsonProperty("result")
	private Result result;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Result {
		private List<StockRequest> list;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class StockRequest {
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
}