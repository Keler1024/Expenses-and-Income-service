package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.model.converter.AccountConverter;
import com.github.keler1024.expensesandincomeservice.model.request.AccountRequest;
import com.github.keler1024.expensesandincomeservice.model.response.AccountResponse;
import com.github.keler1024.expensesandincomeservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService extends BaseService<AccountRequest, Account, AccountResponse, AccountRepository> {

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountConverter accountConverter) {
        super(accountRepository, accountConverter);
    }

    public List<AccountResponse> getByOwnerId(Long ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException();
        }
        List<Account> result = entityRepository.findByOwnerId(ownerId);
        return converter.createResponses(result);
    }

    public AccountResponse add(AccountRequest accountRequest, Long ownerId) {
        if (accountRequest == null || ownerId == null || ownerId < 0) {
            throw new IllegalArgumentException();
        }
        Account newAccount = converter.convertToEntity(accountRequest);
        newAccount.setOwnerId(ownerId);
        return converter.convertToResponse(entityRepository.save(newAccount));
    }

    @Override
    public AccountResponse update(AccountRequest accountRequest, Long id) {
        if (id == null || id < 0 || accountRequest == null) {
            throw new IllegalArgumentException();
        }
        Account account = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Account with id %d not found", id))
        );
        account.setMoney(accountRequest.getMoney());
        account.setName(accountRequest.getName());
        account.setCurrency(Currency.of(accountRequest.getCurrency()));
        return converter.convertToResponse(entityRepository.save(account));
    }
}
