package com.github.keler1024.expensesandincomeservice.data.converter;

import com.github.keler1024.expensesandincomeservice.data.enums.AccountChangeType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class AccountChangeTypeConverter implements AttributeConverter<AccountChangeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccountChangeType accountChangeType) {
        Objects.requireNonNull(accountChangeType);
        return accountChangeType.getCode();
    }

    @Override
    public AccountChangeType convertToEntityAttribute(Integer code) {
        Objects.requireNonNull(code);
        return AccountChangeType.of(code);
    }
}
