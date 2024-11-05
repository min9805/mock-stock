package com.mock.investment.stock.domain.user.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;


@Converter
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserRole attribute) {
        return attribute.getCode();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }

        return UserRole.of(dbData);
    }
}