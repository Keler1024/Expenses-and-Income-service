package com.github.keler1024.expensesandincomeservice.repository;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.repository.specification.SpecificationOnConjunctionBuilder;
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
public class ChangeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChangeRepository changeRepository;

    private final List<Account> accountList = List.of(
            new Account( 1L, 50000L, "Debit card", Currency.RUB),
            new Account(1L, 70000L, "Credit card", Currency.RUB)
    );

    private final List<Category> categoryList = List.of(
            new Category( "Groceries", 0L),
            new Category( "Electronics", 0L)
    );

    private final List<Tag> tagList = List.of(
            new Tag( "Sweets", 1L),
            new Tag( "Gadgets", 1L),
            new Tag( "Baking", 1L)
    );

    private final List<Change> changeList = List.of(
            new Change(
                    accountList.get(0),
                    categoryList.get(0),
                    -1900L,
                    LocalDateTime.of(2022, Month.AUGUST, 19, 17, 9),
                    "Groceries store",
                    "Food",
                    Set.of(tagList.get(0), tagList.get(2))
            ),
            new Change(
                    accountList.get(1),
                    categoryList.get(1),
                    -7000L,
                    LocalDateTime.of(2022, Month.SEPTEMBER, 28, 19, 11 ),
                    "Electronics store",
                    "New standing microphone",
                    Set.of(tagList.get(1))
            ),
            new Change(
                    accountList.get(0),
                    null,
                    45000L,
                    LocalDateTime.of(2022, Month.AUGUST, 27, 10, 0),
                    "Workplace",
                    "Salary",
                    Collections.emptySet()
            ),
            new Change(
                    accountList.get(0),
                    categoryList.get(0),
                    -500L,
                    LocalDateTime.of(2022, Month.SEPTEMBER, 25, 20, 34),
                    "Bakery",
                    "Sweet buns",
                    Set.of(tagList.get(0))
            )
    );

    @BeforeEach
    void setUp() {
        System.out.println("id of account 1 before persisting: " + accountList.get(0).getId());
        System.out.println("id of account 2 before persisting: " + accountList.get(1).getId());
        entityManager.persist(accountList.get(0));
        entityManager.persist(accountList.get(1));
        entityManager.persist(categoryList.get(0));
        entityManager.persist(categoryList.get(1));
        entityManager.persist(tagList.get(0));
        entityManager.persist(tagList.get(1));
        entityManager.persist(tagList.get(2));

        changeRepository.saveAll(changeList);

        System.out.println("id of account 1 after persisting: " + accountList.get(0).getId());
        System.out.println("id of account 2 after persisting: " + accountList.get(1).getId());
    }

    @AfterEach
    void tearDown() {
        changeRepository.deleteAll();
    }

    @Test
    void findAllUnfilteredTest() {
        List<Change> resultList = changeRepository.findAll();
        System.out.println("id of received accountChange 1: " + resultList.get(0).getId());
        System.out.println("id of received accountChange 2: " + resultList.get(1).getId());
        assertNotNull(changeList.get(0));
        assertNotNull(changeList.get(1));
        assertEquals(changeList.size(), resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertNotNull(resultList.get(2));
        assertEquals(changeList.get(0), resultList.get(0));
        assertEquals(changeList.get(1), resultList.get(1));
        assertEquals(changeList.get(2), resultList.get(2));
    }

    @Test
    void findAllFilteredByAccountId() {
        Specification<Change> specification = new SpecificationOnConjunctionBuilder<Change>().nestedEqual(
                List.of("account", "id"), accountList.get(0).getId()).build();
        List<Change> resultList = changeRepository.findAll(specification);
        assertEquals(3, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertEquals(changeList.get(0), resultList.get(0));
        assertEquals(changeList.get(2), resultList.get(1));
    }

    @Test
    void findAllFilteredByAmountLessThanZero() {
        Specification<Change> specification =
                new SpecificationOnConjunctionBuilder<Change>().lessThan("amount", 0).build();
        List<Change> resultList = changeRepository.findAll(specification);
        assertEquals(3, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertNotNull(resultList.get(2));
        assertEquals(changeList.get(0), resultList.get(0));
        assertEquals(changeList.get(1), resultList.get(1));
        assertEquals(changeList.get(3), resultList.get(2));
    }

    @Test
    void findAllFilteredByAccountCategory() {
        Specification<Change> specification = new SpecificationOnConjunctionBuilder<Change>().nestedEqual(
                List.of("category", "id"), categoryList.get(1).getId()).build();
        List<Change> resultList = changeRepository.findAll(specification);
        assertEquals(1, resultList.size());
        assertNotNull(resultList.get(0));
        assertEquals(changeList.get(1), resultList.get(0));
    }

    @Test
    void findAllFilteredByDateTime() {
        Specification<Change> specification = new SpecificationOnConjunctionBuilder<Change>()
                .between(
                        "dateTime",
                        LocalDateTime.of(2022, Month.AUGUST, 1, 0, 0),
                        LocalDateTime.of(2022, Month.AUGUST, 31, 23, 59))
                .build();
        List<Change> resultList = changeRepository.findAll(specification);
        assertEquals(2, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertEquals(changeList.get(0), resultList.get(0));
        assertEquals(changeList.get(2), resultList.get(1));
    }

    @Test
    void findAllFilteredByPlace() {
        Specification<Change> specification = new SpecificationOnConjunctionBuilder<Change>()
                .like("place", "%" + "Groceries" + "%")
                .build();
        List<Change> resultList = changeRepository.findAll(specification);
        assertEquals(1, resultList.size());
        assertNotNull(resultList.get(0));
        assertEquals(changeList.get(0), resultList.get(0));
    }

    @Test
    void findAllFilteredByAmountLessThanZeroAndDateTime() {
        Specification<Change> specification = new SpecificationOnConjunctionBuilder<Change>()
                .lessThan("amount", 0)
                .between(
                        "dateTime",
                        LocalDateTime.of(2022, Month.AUGUST, 1, 0, 0),
                        LocalDateTime.of(2022, Month.AUGUST, 31, 23, 59)
                )
                .build();
        List<Change> resultList = changeRepository.findAll(specification);
        assertEquals(1, resultList.size());
        assertNotNull(resultList.get(0));
        assertEquals(changeList.get(0), resultList.get(0));
    }

    @Test
    void findAllFilteredByTag() {
        Specification<Change> specification = new SpecificationOnConjunctionBuilder<Change>()
                .containsAllTags(Set.of(tagList.get(0))).build();
        List<Change> resultList = changeRepository.findAll(specification);
        assertEquals(2, resultList.size());
        assertNotNull(resultList.get(0));
        assertNotNull(resultList.get(1));
        assertEquals(changeList.get(0), resultList.get(0));
        assertEquals(changeList.get(3), resultList.get(1));

        specification = new SpecificationOnConjunctionBuilder<Change>()
                .containsAllTags(Set.of(tagList.get(0), tagList.get(2))).build();
        resultList = changeRepository.findAll(specification);
        assertEquals(1, resultList.size());
        assertNotNull(resultList.get(0));
        assertEquals(changeList.get(0), resultList.get(0));
    }
}
