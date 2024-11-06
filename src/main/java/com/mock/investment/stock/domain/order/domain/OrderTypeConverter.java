package com.mock.investment.stock.domain.order.domain;

import jakarta.persistence.AttributeConverter;

public class OrderTypeConverter implements AttributeConverter<OrderType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderType orderType) {
        return orderType.getCode();
    }

    @Override
    public OrderType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return OrderType.of(dbData);
    }
}
