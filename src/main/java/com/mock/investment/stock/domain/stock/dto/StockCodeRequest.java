package com.mock.investment.stock.domain.stock.dto;

import com.mock.investment.stock.domain.stock.domain.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockCodeRequest {
	private String market;

	private String korean_name;

	private String english_name;

	public Stock toEntity() {
		return Stock.builder()
				.code(market)
				.koreanName(korean_name)
				.englishName(english_name)
				.build();
	}
}