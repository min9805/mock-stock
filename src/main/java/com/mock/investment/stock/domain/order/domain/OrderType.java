package com.mock.investment.stock.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderType {
    LIMIT(0, "지정가"),
    MARKET(1, "시장가");

    private final int code;
    private final String description;

    public static OrderType of(int code) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.getCode() == code) {
                return orderType;
            }
        }
        //TODO
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
