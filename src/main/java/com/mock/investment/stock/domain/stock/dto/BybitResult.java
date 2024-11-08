package com.mock.investment.stock.domain.stock.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BybitResult {
	private String category;
	private List<StockCodeRequestFromBybit> list;
}
