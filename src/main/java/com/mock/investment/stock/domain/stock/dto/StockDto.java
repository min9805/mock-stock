package com.mock.investment.stock.domain.stock.dto;

import com.mock.investment.stock.domain.stock.domain.Stock;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StockDto {
	private String market;
	private String koreanName;
	private String englishName;

	public static StockDto fromEntity(Stock stock) {
		return StockDto.builder()
				.market(stock.getCode())
				.koreanName(stock.getKoreanName())
				.englishName(stock.getEnglishName())
				.build();
	}

	public static List<StockDto> fromEntities(List<Stock> stockList) {
		return stockList.stream()
				.map(StockDto::fromEntity)
				.toList();
	}
}