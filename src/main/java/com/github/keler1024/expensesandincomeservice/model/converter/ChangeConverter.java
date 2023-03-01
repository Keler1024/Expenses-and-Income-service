package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.model.request.ChangeRequest;
import com.github.keler1024.expensesandincomeservice.model.response.ChangeResponse;
import com.github.keler1024.expensesandincomeservice.repository.AccountRepository;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChangeConverter
        implements IRequestToEntityToResponseConverter<ChangeRequest, Change, ChangeResponse> {
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ChangeConverter(
            CategoryRepository categoryRepository,
            TagRepository tagRepository,
            AccountRepository accountRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public final Change convertToEntity(final ChangeRequest request) {
        if (request == null || request.getAccountId() == null || request.getAccountId() < 0) {
            throw new IllegalArgumentException();
        }
        Change change = new Change();
        change.setAmount(request.getAmount());
        change.setDateTime(request.getDateTime());
        change.setPlace(request.getPlace());
        change.setComment(request.getComment());
        change.setAccount(accountRepository.findById(request.getAccountId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Account with id " + request.getAccountId() + " not found"
                )
        ));
        if (request.getCategoryId() != null && request.getCategoryId() >= 0) {
            change.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Category with id " + request.getCategoryId() + " not found"
                    )
            ));
        }
        if (request.getTagIds() != null) {
            change.setTags(Set.copyOf(tagRepository.findAllById(request.getTagIds())));
        } else {
            change.setTags(Collections.emptySet());
        }
        return change;
    }

    @Override
    public final ChangeResponse convertToResponse(final Change change) {
        if (change == null) {
            throw new NullPointerException();
        }
        return new ChangeResponse(
                change.getId(),
                change.getAccount().getId(),
                change.getCategory().getId(),
                change.getAmount(),
                change.getDateTime(),
                change.getTags().stream().map(Tag::getId).collect(Collectors.toSet()),
                change.getPlace(),
                change.getComment()
        );
    }
}
