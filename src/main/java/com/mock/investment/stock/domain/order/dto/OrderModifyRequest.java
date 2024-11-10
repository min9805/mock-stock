package com.mock.investment.stock.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderModifyRequest {
    @Schema(description = "주문 번호", example = "1")
    private Long orderId;

    @Schema(description = "변경 주문 가격", example = "14000")
    private Double price;
}
