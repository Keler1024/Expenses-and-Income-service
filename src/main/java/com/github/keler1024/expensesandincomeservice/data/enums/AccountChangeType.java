package com.github.keler1024.expensesandincomeservice.data.enums;

import java.util.stream.Stream;

public enum AccountChangeType {
    INCOME(1), EXPENSE(0);

    private final int code;

    AccountChangeType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AccountChangeType of(final int code) {
        return Stream.of(AccountChangeType.values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
