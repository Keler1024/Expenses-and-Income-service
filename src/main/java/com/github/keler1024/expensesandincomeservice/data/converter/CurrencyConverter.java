package com.github.keler1024.expensesandincomeservice.data.converter;

import com.github.keler1024.expensesandincomeservice.data.enums.Currency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, String> {
    @Override
    public String convertToDatabaseColumn(Currency currency) {
        Objects.requireNonNull(currency);
        return currency.getCurrencyCode();
    }

    @Override
    public Currency convertToEntityAttribute(String s) {
        return Currency.of(s);
    }
}
