package com.github.keler1024.expensesandincomeservice.data.configuration;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChange;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeCategory;
import com.github.keler1024.expensesandincomeservice.data.enums.AccountChangeType;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.repository.AccountChangeCategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.AccountChangeRepository;
import com.github.keler1024.expensesandincomeservice.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

@Configuration
class PreloadDatabase {

    private static final Logger log = LoggerFactory.getLogger(PreloadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            AccountChangeRepository accountChangeRepository,
            AccountRepository accountRepository,
            AccountChangeCategoryRepository accountChangeCategoryRepository) {
        return args -> {
            Account accountDebit = new Account(1L, 5000L, "Debit card", Currency.RUB);
            Account accountCredit = new Account(1L, 60000L, "Credit card", Currency.RUB);
            accountRepository.save(accountDebit);
            accountRepository.save(accountCredit);

            accountRepository.findAll().forEach(account -> log.info("Preloaded " + account));

            AccountChangeCategory groceriesCategory = new AccountChangeCategory("Groceries", 0L);
            AccountChangeCategory electronicsCategory = new AccountChangeCategory("Electronics", 0L);
            accountChangeCategoryRepository.save(groceriesCategory);
            accountChangeCategoryRepository.save(electronicsCategory);

            accountChangeCategoryRepository.findAll().forEach(accountChangeCategory ->
                    log.info("Preloaded " + accountChangeCategory)
            );

            accountChangeRepository.save(new AccountChange(
                    accountDebit,
                    AccountChangeType.EXPENSE,
                    groceriesCategory,
                    500L,
                    LocalDateTime.of(2022, Month.AUGUST, 26, 17, 23),
                    "Grocery store",
                    "Food",
                    Collections.emptySet()
            ));
            accountChangeRepository.save(new AccountChange(
                    accountCredit,
                    AccountChangeType.EXPENSE,
                    electronicsCategory,
                    3000L,
                    LocalDateTime.of(2022, Month.SEPTEMBER, 14, 19, 7),
                    "Electronics store",
                    "new web camera for home pc",
                    Collections.emptySet()
            ));
            accountChangeRepository.findAll().forEach(accountChange -> log.info("Preloaded " + accountChange));
        };
    }
}
