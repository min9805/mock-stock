package com.mock.investment.stock.domain.stock.dto;

import com.mock.investment.stock.domain.stock.domain.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 주식 코드 요청 DTO from Upbit
 *
 * [{"market":"KRW-BTC","korean_name":"비트코인","english_name":"Bitcoin"}, ... ]
 */

@Data
@NoArgsConstructor
public class StockCodeRequestFromUpbit {
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