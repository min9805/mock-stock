package com.mock.investment.stock.domain.user.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;


@Converter
public class OAuthProviderConverter implements AttributeConverter<OAuthProvider, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OAuthProvider attribute) {
        return attribute.getCode();
    }

    @Override
    public OAuthProvider convertToEntityAttribute(Integer dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }

        return OAuthProvider.of(dbData);
    }
}