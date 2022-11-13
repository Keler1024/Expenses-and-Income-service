package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeCategory;
import com.github.keler1024.expensesandincomeservice.repository.AccountChangeCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountChangeCategoryService {
    private final AccountChangeCategoryRepository accountChangeCategoryRepository;

    public AccountChangeCategoryService(AccountChangeCategoryRepository accountChangeCategoryRepository) {
        this.accountChangeCategoryRepository = accountChangeCategoryRepository;
    }

    public List<AccountChangeCategory> getAccountChangeCategories() {
        return accountChangeCategoryRepository.findAll();
    }
}
