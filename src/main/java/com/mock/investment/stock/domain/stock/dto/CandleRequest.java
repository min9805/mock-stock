package com.mock.investment.stock.domain.stock.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandleRequest {
    @Schema(description = "심볼", example = "BTCUSDT")
    private String symbol;

    @Schema(description = "시간 간격", example = "1")
    private String interval;

    @Schema(description = "시간 단위", example = "m", allowableValues = {"m", "h", "d", "w", "M"})
    private Character unit;

    @Schema(description = "종료 시간 (timestamp in seconds)", example = "1735689600000")  // 2025-01-01 00:00:00
    private long endTime;

    @Schema(description = "조회할 캔들 개수", example = "200", defaultValue = "200")
    private int limit = 200;

}
