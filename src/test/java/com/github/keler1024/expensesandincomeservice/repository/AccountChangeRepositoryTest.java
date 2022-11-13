package com.github.keler1024.expensesandincomeservice.repository;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChange;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeCategory;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeTag;
import com.github.keler1024.expensesandincomeservice.data.enums.AccountChangeType;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.repository.specification.JPASpecifications;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountChangeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountChangeRepository accountChangeRepository;

    private final List<Account> accountList = List.of(
            new Account( 1L, 50000L, "Debit card", Currency.RUB),
            new Account(1L, 70000L, "Credit card", Currency.RUB)
    );

    private final List<AccountChangeCategory> accountChangeCategoryList = List.of(
            new AccountChangeCategory( "Groceries", 0L),
            new AccountChangeCategory( "Electronics", 0L)
    );

    private final List<AccountChangeTag> accountChangeTagList = List.of(
            new AccountChangeTag( "Sweets", 1L),
            new AccountChangeTag( "Gadgets", 1L),
            new AccountChangeTag( "Baking", 1L)
    );

    private final List<AccountChange> accountChangeList = List.of(
            new AccountChange(
                    accountList.get(0),
                    AccountChangeType.EXPENSE,
                    accountChangeCategoryList.get(0),
                    1900L,
                    LocalDateTime.of(2022, Month.AUGUST, 19, 17, 9),
                    "Groceries store",
                    "Food",
                    Set.of(accountChangeTagList.get(0), accountChangeTagList.get(2))
            ),
            new AccountChange(
                    accountList.get(1),
                    AccountChangeType.EXPENSE,
                    accountChangeCategoryList.get(1),
                    7000L,
                    LocalDateTime.of(2022, Month.SEPTEMBER, 28, 19, 11 ),
                    "Electronics store",
                    "New standing microphone",
                    Set.of(accountChangeTagList.get(1))
            ),
            new AccountChange(
                    accountList.get(0),
                    AccountChangeType.INCOME,
                    null,
                    45000L,
                    LocalDateTime.of(2022, Month.AUGUST, 27, 10, 0),
                    "Workplace",
                    "Salary",
                    Collections.emptySet()
            ),
            new AccountChange(
                    accountList.get(0),
                    AccountChangeType.EXPENSE,
                    accountChangeCategoryList.get(0),
                    500L,
                    LocalDateTime.of(2022, Month.SEPTEMBER, 25, 20, 34),
                    "Bakery",
                    "Sweet buns",
                    Set.of(accountChangeTagList.get(0))
            )
    );

    @BeforeEach
    void setUp() {
        System.out.println("id of account 1 before persisting: " + accountList.get(0).getId());
        System.out.println("id of account 2 before persisting: " + accountList.get(1).getId());
        entityManager.persist(accountList.get(0));
        entityManager.persist(accountList.get(1));
        entityManager.persist(accountChangeCategoryList.get(0));
        entityManager.persist(accountChangeCategoryList.get(1));
        entityManager.persist(accountChangeTagList.get(0));
        entityManager.persist(accountChangeTagList.get(1));
        entityManager.persist(accountChangeTagList.get(2));

        accountChangeRepository.saveAll(accountChangeList);

        System.out.println("id of account 1 after persisting: " + accountList.get(0).getId());
        System.out.println("id of account 2 after persisting: " + accountList.get(1).getId());
    }

    @AfterEach
    void tearDown() {
        accountChangeRepository.deleteAll();
    }

    @Test
    void findAllUnfilteredTest() {
        List<AccountChange> resultList = accountChangeRepository.findAll();
        System.out.println("id of received accountChange 1: " + resultList.get(0).getId());
        System.out.println("id of received accountChange 2: " + resultList.get(1).getId());
        assertNotNull(accountChangeList.get(0));
        assertNotNull(accountChangeList.get(1));
        assertEquals(3, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertNotNull(resultList.get(2));
        assertEquals(accountChangeList.get(0), resultList.get(0));
        assertEquals(accountChangeList.get(1), resultList.get(1));
        assertEquals(accountChangeList.get(2), resultList.get(2));
    }

    @Test
    void findAllFilteredByAccountId() {
        Specification<AccountChange> specification = JPASpecifications.nestedEqual(
                List.of("account", "id"), accountList.get(0).getId());
        List<AccountChange> resultList = accountChangeRepository.findAll(specification);
        assertEquals(2, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertEquals(accountChangeList.get(0), resultList.get(0));
        assertEquals(accountChangeList.get(2), resultList.get(1));
    }

    @Test
    void findAllFilteredByAccountChangeType() {
        Specification<AccountChange> specification = JPASpecifications.equal("changeType", AccountChangeType.EXPENSE);
        List<AccountChange> resultList = accountChangeRepository.findAll(specification);
        assertEquals(2, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertEquals(accountChangeList.get(0), resultList.get(0));
        assertEquals(accountChangeList.get(1), resultList.get(1));
    }

    @Test
    void findAllFilteredByAccountCategory() {
        Specification<AccountChange> specification = JPASpecifications.nestedEqual(
                List.of("category", "id"), accountChangeCategoryList.get(1).getId());
        List<AccountChange> resultList = accountChangeRepository.findAll(specification);
        assertEquals(1, resultList.size());
        assertNotNull(resultList.get(0));
        assertEquals(accountChangeList.get(1), resultList.get(0));
    }

    @Test
    void findAllFilteredByDateTime() {
        Specification<AccountChange> specification = JPASpecifications.between(
                "dateTime",
                LocalDateTime.of(2022, Month.AUGUST, 1, 0, 0),
                LocalDateTime.of(2022, Month.AUGUST, 31, 23, 59));
        List<AccountChange> resultList = accountChangeRepository.findAll(specification);
        assertEquals(2, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertEquals(accountChangeList.get(0), resultList.get(0));
        assertEquals(accountChangeList.get(2), resultList.get(1));
    }

    @Test
    void findAllFilteredByPlace() {
        Specification<AccountChange> specification = JPASpecifications.like("place", "%" + "Groceries" + "%");
        List<AccountChange> resultList = accountChangeRepository.findAll(specification);
        assertEquals(1, resultList.size());
        assertNotNull(resultList.get(0));
        assertEquals(accountChangeList.get(0), resultList.get(0));
    }

    @Test
    void findAllFilteredByAccountChangeTypeAndDateTime() {
        Specification<AccountChange> specification = JPASpecifications.equal("changeType", AccountChangeType.EXPENSE);
        specification = specification.and(JPASpecifications.between(
                "dateTime",
                LocalDateTime.of(2022, Month.AUGUST, 1, 0, 0),
                LocalDateTime.of(2022, Month.AUGUST, 31, 23, 59)
        ));
        List<AccountChange> resultList = accountChangeRepository.findAll(specification);
        assertEquals(1, resultList.size());
        assertNotNull(resultList.get(0));
        assertEquals(accountChangeList.get(0), resultList.get(0));
    }

    @Test
    void findAllFilteredByTag() {
        Specification<AccountChange> specification =
                JPASpecifications.containsAllTags(Set.of(accountChangeTagList.get(0)));
        List<AccountChange> resultList = accountChangeRepository.findAll(specification);
        assertEquals(2, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertEquals(accountChangeList.get(0), resultList.get(0));
        assertEquals(accountChangeList.get(3), resultList.get(1));

        specification =
                JPASpecifications.containsAllTags(Set.of(accountChangeTagList.get(0), accountChangeTagList.get(2)));
        resultList = accountChangeRepository.findAll(specification);
        assertEquals(1, resultList.size());
        assertNotNull(resultList.get(0));
        assertEquals(accountChangeList.get(0), resultList.get(0));
    }
}
