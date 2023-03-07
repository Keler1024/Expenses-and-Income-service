package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.AccountConverter;
import com.github.keler1024.expensesandincomeservice.model.request.AccountRequest;
import com.github.keler1024.expensesandincomeservice.model.response.AccountResponse;
import com.github.keler1024.expensesandincomeservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService extends EntityService<AccountRequest, Account, AccountResponse, AccountRepository> {

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountConverter accountConverter) {
        super(accountRepository, accountConverter);
    }

    public List<AccountResponse> getByOwnerId() {
        Long ownerId = getAuthenticatedUserId();
        List<Account> result = entityRepository.findByOwnerId(ownerId);
        return converter.createResponses(result);
    }

    @Override
    protected void performUpdate(Account entity, AccountRequest request) {
        entity.setMoney(request.getMoney());
        entity.setName(request.getName());
        entity.setCurrency(Currency.of(request.getCurrency()));
    }

    @Override
    protected void performOnAdd(Account entity) {
        Long ownerId = getAuthenticatedUserId();
        entity.setOwnerId(ownerId);
    }

    @Override
    protected Long getEntityOwnerId(Account entity) {
        return entity.getOwnerId();
    }
}
