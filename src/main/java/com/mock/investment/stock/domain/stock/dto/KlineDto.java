package com.mock.investment.stock.domain.stock.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class KlineDto {
    private String symbol;
    private List<CandleStick> list;
}
