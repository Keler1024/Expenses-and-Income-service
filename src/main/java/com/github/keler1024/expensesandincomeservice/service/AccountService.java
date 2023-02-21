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
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountConverter accountConverter;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountConverter accountConverter) {
        this.accountRepository = accountRepository;
        this.accountConverter = accountConverter;
    }

    public AccountResponse getAccount(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return accountConverter.convertToResponse(
                accountRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Account with id %d not found", id))
                ));
    }

    public List<AccountResponse> getAccountsByOwnerId(Long ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException();
        }
        List<Account> result = accountRepository.findByOwnerId(ownerId);
        return accountConverter.createResponses(result);
    }

    public AccountResponse addAccount(AccountRequest accountRequest, Long ownerId) {
        if (accountRequest == null || ownerId == null || ownerId < 0) {
            throw new IllegalArgumentException();
        }
        Account newAccount = accountConverter.convertToEntity(accountRequest);
        newAccount.setOwnerId(ownerId);
        return accountConverter.convertToResponse(accountRepository.save(newAccount));
    }

    public AccountResponse updateAccount(AccountRequest accountRequest, Long id) {
        if (id == null || id < 0 || accountRequest == null) {
            throw new IllegalArgumentException();
        }
        return accountRepository.findById(id)
                .map(account -> {
                    account.setMoney(accountRequest.getMoney());
                    account.setName(accountRequest.getName());
                    account.setCurrency(Currency.of(accountRequest.getCurrency()));
                    return accountConverter.convertToResponse(accountRepository.save(account));
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Account with id %d not found", id)));
    }

    public void deleteAccount(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Null instead of Account id provided");
        }
        if(!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account with id " + id + " not found");
        }
        accountRepository.deleteById(id);
    }
}
