package com.github.keler1024.expensesandincomeservice.model.converter;


import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.model.request.AccountRequest;
import static org.junit.jupiter.api.Assertions.*;

import com.github.keler1024.expensesandincomeservice.model.response.AccountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class AccountConverterTest {

    static AccountConverter accountConverter = new AccountConverter();

    @Test
    void givenValidRequest_convertToEntity() {
        AccountRequest accountRequest = new AccountRequest(100L, "Card", Currency.RUB.getCurrencyCode());
        Account account = accountConverter.convertToEntity(accountRequest);
        assertEquals(accountRequest.getMoney(), account.getMoney());
        assertEquals(accountRequest.getName(), account.getName());
        assertEquals(accountRequest.getCurrency(), account.getCurrency().getCurrencyCode());
    }

    @Test
    void givenValidEntity_convertToResponse() {
        Account account = new Account(2L, 1L, 100L, "Card", Currency.RUB);
        AccountResponse accountResponse = accountConverter.convertToResponse(account);
        assertEquals(accountResponse.getId(), account.getId());
        assertEquals(accountResponse.getMoney(), account.getMoney());
        assertEquals(accountResponse.getName(), account.getName());
        assertEquals(accountResponse.getCurrency(), account.getCurrency().getCurrencyCode());
    }

    @Test
    void givenNullArgument_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> accountConverter.convertToEntity(null));
        assertThrows(NullPointerException.class, () -> accountConverter.convertToResponse(null));
    }

}
