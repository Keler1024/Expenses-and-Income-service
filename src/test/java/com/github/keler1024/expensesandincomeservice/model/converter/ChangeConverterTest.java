package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.*;
import com.github.keler1024.expensesandincomeservice.data.enums.Currency;
import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.request.ChangeRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import com.github.keler1024.expensesandincomeservice.model.response.ChangeResponse;
import com.github.keler1024.expensesandincomeservice.repository.AccountRepository;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChangeConverterTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    ChangeConverter changeConverter;

    Account account = new Account(2L, 1L, 57030L, "TestAccount", Currency.RUB);

    Category category = new Category(3L, "TestCategory", 1L);

    Set<Tag> tagSet = Set.of(new Tag(4L, "TestTag", 1L));

    void testRequestToEntity(ChangeRequest changeRequest, Change change) {
        assertEquals(changeRequest.getAccountId(), change.getAccount().getId());
        if (changeRequest.getCategoryId() != null) {
            assertNotNull(change.getCategory());
            assertEquals(changeRequest.getCategoryId(), change.getCategory().getId());
        }
        else {
            assertNull(change.getCategory());
        }
        if (changeRequest.getTagIds() != null) {
            assertNotNull(change.getTags());
            assertEquals(changeRequest.getTagIds(),
                    change.getTags().stream().map(Tag::getId).collect(Collectors.toSet()));
        }
        else {
            assertTrue(change.getTags().isEmpty());
        }
        assertEquals(changeRequest.getAmount(), change.getAmount());
        assertEquals(changeRequest.getDateTime(), change.getDateTime());
        assertEquals(changeRequest.getPlace(), change.getPlace());
        assertEquals(changeRequest.getComment(), change.getComment());
    }

    void testEntityToResponse(Change change, ChangeResponse changeResponse) {
        assertEquals(change.getAccount().getId(), changeResponse.getAccountId());
        if (change.getCategory() != null) {
            assertEquals(change.getCategory().getId(), changeResponse.getCategoryId());
        }
        else {
            assertNull(changeResponse.getCategoryId());
        }
        if (change.getTags() != null) {
            assertEquals(change.getTags().stream().map(Tag::getId).collect(Collectors.toSet()),
                    changeResponse.getTagIds());
        }
        else {
            assertTrue(change.getTags().isEmpty());
        }
        assertEquals(change.getAmount(), changeResponse.getAmount());
        assertEquals(change.getDateTime(), changeResponse.getDateTime());
        assertEquals(change.getPlace(), changeResponse.getPlace());
        assertEquals(change.getComment(), changeResponse.getComment());
    }

    @Test
    void givenValidBudgetRequest_convertToEntity() {
        ChangeRequest changeRequest = new ChangeRequest(
                account.getId(),
                category.getId(),
                2300L,
                LocalDateTime.of(2022, 8, 1, 12, 30, 45),
                tagSet.stream().map(Tag::getId).collect(Collectors.toSet()),
                "TestPlace",
                "TestComment");

        when(accountRepository.findById(changeRequest.getAccountId())).thenReturn(Optional.of(account));
        when(categoryRepository.findById(changeRequest.getCategoryId())).thenReturn(Optional.of(category));
        when(tagRepository.findAllById(changeRequest.getTagIds())).thenReturn(List.copyOf(tagSet));
        Change change = changeConverter.convertToEntity(changeRequest);

        testRequestToEntity(changeRequest, change);
    }

    @Test
    void givenValidBudgetEntity_convertToResponse() {
        Change change = new Change(
                3L,
                account,
                category,
                2300L,
                LocalDateTime.of(2022, 8, 1, 12, 30, 45),
                "TestPlace",
                "TestComment",
                tagSet);

        ChangeResponse changeResponse = changeConverter.convertToResponse(change);

        testEntityToResponse(change, changeResponse);
    }

    @Test
    void givenNullArgument_throwNullPointerException() {
        assertThrows(IllegalArgumentException.class, () -> changeConverter.convertToEntity(null));
        assertThrows(NullPointerException.class, () -> changeConverter.convertToResponse(null));
    }
}
