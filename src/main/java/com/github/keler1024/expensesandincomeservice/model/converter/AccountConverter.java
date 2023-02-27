package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.model.request.AccountRequest;
import com.github.keler1024.expensesandincomeservice.model.response.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter implements IRequestToEntityToResponseConverter<AccountRequest, Account, AccountResponse>{

    @Override
    public Account convertToEntity(AccountRequest request) {
        if (request == null) {
            throw new NullPointerException();
        }
        Account account = new Account();
        account.setName(request.getName());
        account.setMoney(request.getMoney());
        account.setCurrency(Currency.of(request.getCurrency()));
        return account;
    }

    @Override
    public AccountResponse convertToResponse(Account entity) {
        if (entity == null) {
            throw new NullPointerException();
        }
        return new AccountResponse(
                entity.getId(),
                entity.getMoney(),
                entity.getName(),
                entity.getCurrency().getCurrencyCode()
        );
    }
}
