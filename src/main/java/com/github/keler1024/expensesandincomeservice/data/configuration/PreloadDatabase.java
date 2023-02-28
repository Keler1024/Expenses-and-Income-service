package com.github.keler1024.expensesandincomeservice.data.configuration;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.ChangeRepository;
import com.github.keler1024.expensesandincomeservice.repository.AccountRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
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
            ChangeRepository changeRepository,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository) {
        return args -> {
            Account accountDebit = new Account(1L, 5000L, "Debit card", Currency.RUB);
            Account accountCredit = new Account(1L, 60000L, "Credit card", Currency.RUB);
            accountRepository.save(accountDebit);
            accountRepository.save(accountCredit);

            accountRepository.findAll().forEach(account -> log.info("Preloaded " + account));

            Category groceriesCategory = new Category("Groceries", null);
            Category electronicsCategory = new Category("Electronics", null);
            categoryRepository.save(groceriesCategory);
            categoryRepository.save(electronicsCategory);

            categoryRepository.findAll().forEach(category ->
                    log.info("Preloaded " + category)
            );

            changeRepository.save(new Change(
                    accountDebit,
                    groceriesCategory,
                    -500L,
                    LocalDateTime.of(2022, Month.AUGUST, 26, 17, 23),
                    "Grocery store",
                    "Food",
                    Collections.emptySet()
            ));
            changeRepository.save(new Change(
                    accountCredit,
                    electronicsCategory,
                    -3000L,
                    LocalDateTime.of(2022, Month.SEPTEMBER, 14, 19, 7),
                    "Electronics store",
                    "new web camera for home pc",
                    Collections.emptySet()
            ));
            changeRepository.findAll().forEach(change -> log.info("Preloaded " + change));

            tagRepository.save(new Tag("Project", 1L));
            tagRepository.findAll().forEach(tag -> log.info("Preloaded " + tag));
        };
    }
}
