package com.github.keler1024.expensesandincomeservice.data.enums;

import java.util.Objects;
import java.util.stream.Stream;

//Replacement for proper currency representation
public enum Currency {
    RUB("RUB"), EUR("EUR"), USD("USD");

    private final String currencyCode;
    Currency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public static Currency of(String currencyCode) {
        Objects.requireNonNull(currencyCode);
        return Stream.of(Currency.values())
                .filter(c -> c.getCurrencyCode().equals(currencyCode))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
