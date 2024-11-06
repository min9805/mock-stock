package com.mock.investment.stock.domain.stock.dto;

import com.mock.investment.stock.domain.stock.domain.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockCodeRequest {
	private String market;

	private String koreanName;

	private String englishName;

	public Stock toEntity() {
		return Stock.builder()
				.code(market)
				.koreanName(koreanName)
				.englishName(englishName)
				.build();
	}
}