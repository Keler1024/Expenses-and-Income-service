package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.*;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.ChangeRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetConverterTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    ChangeRepository changeRepository;

    @InjectMocks
    BudgetConverter budgetConverter;

    Account account = new Account(2L, 1L, 100L, "Card", Currency.RUB);

    Category category = new Category(2L, "TestCategory", 1L);

    Tag tag = new Tag(2L, "TestTag", 1L);

    List<Change> changeList = List.of(
            new Change(
                    3L,
                    account,
                    category,
                    -2300L,
                    LocalDateTime.of(2022, 8, 1, 12, 30, 45),
                    "TestPlace",
                    "TestComment",
                    Collections.emptySet()
            ),
            new Change(
                    4L,
                    account,
                    category,
                    -550L,
                    LocalDateTime.of(2022, 8, 2, 17, 24, 1),
                    "TestPlace2",
                    "TestComment2",
                    Set.of(tag)
            )
    );

    void testRequestToEntity(BudgetRequest budgetRequest, Budget budget) {
        assertEquals(budgetRequest.getSize(), budget.getSize());
        assertEquals(budgetRequest.getStartDate(), budget.getStartDate());
        assertEquals(budgetRequest.getEndDate(), budget.getEndDate());
        if (budgetRequest.getCategoryId() != null) {
            assertNotNull(budget.getCategory());
            assertEquals(budgetRequest.getCategoryId(), budget.getCategory().getId());
        }
        else {
            assertNull(budget.getCategory());
        }
        if (budgetRequest.getTagId() != null) {
            assertNotNull(budget.getTag());
            assertEquals(budgetRequest.getTagId(), budget.getTag().getId());
        }
        else {
            assertNull(budget.getTag());
        }
    }

    void testEntityToResponse(Budget budget, BudgetResponse budgetResponse) {
        assertEquals(budget.getId(), budgetResponse.getId());
        assertEquals(budget.getSize(), budgetResponse.getSize());
        assertEquals(budget.getStartDate(), budgetResponse.getStartDate());
        assertEquals(budget.getEndDate(), budgetResponse.getEndDate());
        assertEquals(changeList.stream().mapToLong(Change::getAmount).sum(), budgetResponse.getSpent());
        if (budget.getCategory() != null) {
            assertEquals(budget.getCategory().getId(), budgetResponse.getCategoryId());
        }
        else {
            assertNull(budgetResponse.getCategoryId());
        }
        if (budget.getTag() != null) {
            assertEquals(budget.getTag().getId(), budgetResponse.getTagId());
        }
        else {
            assertNull(budgetResponse.getTagId());
        }
    }

    @Test
    void givenValidBudgetRequest_convertToEntity() {
        BudgetRequest budgetRequest = new BudgetRequest(
                105000L,
                LocalDate.of(2022, 8, 1),
                LocalDate.of(2022, 10, 31),
                category.getId(),
                null);
        when(categoryRepository.findById(budgetRequest.getCategoryId())).thenReturn(Optional.of(category));
        Budget budget = budgetConverter.convertToEntity(budgetRequest);
        testRequestToEntity(budgetRequest, budget);

        budgetRequest = new BudgetRequest(
                35000L,
                LocalDate.of(2021, 7, 14),
                LocalDate.of(2021, 9, 14),
                null,
                tag.getId());

        when(tagRepository.findById(budgetRequest.getTagId())).thenReturn(Optional.of(tag));
        budget = budgetConverter.convertToEntity(budgetRequest);

        testRequestToEntity(budgetRequest, budget);
    }

    @Test
    void givenValidBudgetEntity_convertToResponse() {
        Budget budget = new Budget(
                2L,
                13500L,
                LocalDate.of(2022, 8, 1),
                LocalDate.of(2022, 10, 31),
                category,
                null,
                1L);

        when(changeRepository.findAll(ArgumentMatchers.<Specification<Change>>any())).thenReturn(changeList);
        BudgetResponse budgetResponse = budgetConverter.convertToResponse(budget);

        testEntityToResponse(budget, budgetResponse);

        budget = new Budget(
                2L,
                3500L,
                LocalDate.of(2022, 3, 1),
                LocalDate.of(2022, 5, 24),
                null,
                tag,
                1L);

        budgetResponse = budgetConverter.convertToResponse(budget);

        testEntityToResponse(budget, budgetResponse);
    }

    @Test
    void givenNullArgument_throwNullPointerException() {
        assertThrows(IllegalArgumentException.class, () -> budgetConverter.convertToEntity(null));
        assertThrows(NullPointerException.class, () -> budgetConverter.convertToResponse(null));
    }
}
