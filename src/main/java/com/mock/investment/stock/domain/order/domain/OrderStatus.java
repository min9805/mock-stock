package com.mock.investment.stock.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    PENDING(0, "주문 대기"),
    PARTIALLY_FILLED(1, "부분 체결"),
    COMPLETED(2, "체결"),
    CANCELED(3, "취소"),
    REJECTED(4, "거부"),
    EXPIRED(5, "만료");

    private final int code;
    private final String description;

    public static OrderStatus of(int code) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getCode() == code) {
                return orderStatus;
            }
        }
        //TODO
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
