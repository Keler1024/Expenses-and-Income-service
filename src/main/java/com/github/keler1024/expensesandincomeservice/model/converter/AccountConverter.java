package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.model.request.AccountRequest;
import com.github.keler1024.expensesandincomeservice.model.response.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter extends RequestToEntityToResponseConverter<AccountRequest, Account, AccountResponse>{

    public AccountConverter() {
        super(AccountConverter::convertFromRequest, AccountConverter::convertFromEntity);
    }

    private static Account convertFromRequest(AccountRequest accountRequest) {
        Account account = new Account();
        account.setName(accountRequest.getName());
        account.setMoney(accountRequest.getMoney());
        account.setCurrency(Currency.of(accountRequest.getCurrency()));
        return account;
    }

    private static AccountResponse convertFromEntity(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getMoney(),
                account.getName(),
                account.getCurrency().getCurrencyCode()
        );
    }
}
