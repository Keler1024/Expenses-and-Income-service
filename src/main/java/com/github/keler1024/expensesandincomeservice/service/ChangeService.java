package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.ChangeConverter;
import com.github.keler1024.expensesandincomeservice.model.request.ChangeRequest;
import com.github.keler1024.expensesandincomeservice.model.response.ChangeResponse;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.ChangeRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import com.github.keler1024.expensesandincomeservice.repository.AccountRepository;
import com.github.keler1024.expensesandincomeservice.repository.specification.SpecificationOnConjunctionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
public class ChangeService extends BaseService<ChangeRequest, Change, ChangeResponse, ChangeRepository>{

    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Autowired
    public ChangeService(
            ChangeRepository changeRepository,
            ChangeConverter changeConverter,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository
    ) {
        super(changeRepository, changeConverter);
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public List<ChangeResponse> getAllChanges(
            Long accountId,
            Long amount,
            String comparison,
            Long categoryId,
            String place,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Set<Long> tagIds
    ) {
        Long ownerId = getAuthenticatedUserId();
        SpecificationOnConjunctionBuilder<Change> specificationBuilder = new SpecificationOnConjunctionBuilder<>();
        specificationBuilder.nestedEqual(List.of("account", "ownerId"), ownerId);
        if (accountId != null) {
            specificationBuilder.nestedEqual(List.of("account", "id"), accountId);
        }
        if (amount != null && comparison != null) {
            switch (comparison) {
                case "less":
                    specificationBuilder.lessThan("amount", amount);
                    break;
                case "more":
                    specificationBuilder.greaterThanOrEqualTo("amount", amount);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported comparison provided");
            }
        }
        if (categoryId != null) {
            specificationBuilder.nestedEqual(List.of("category", "id"), categoryId);
        }
        if (place != null && !place.isEmpty()) {
            specificationBuilder.like("place", place);
        }
        if (startDate != null && endDate != null) {
            specificationBuilder.between("dateTime", startDate, endDate);
        }
        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tags = tagRepository.findByIdIn(tagIds);
            specificationBuilder.containsAllTags(tags);
        }
        return converter.createResponses(entityRepository.findAll(specificationBuilder.build()));
    }

    @Override
    public ChangeResponse add(ChangeRequest changeRequest) {
        if (changeRequest == null) {
            throw new IllegalArgumentException();
        }

        Change change = converter.convertToEntity(changeRequest);
        Long ownerId = getAuthenticatedUserId();
        if (!ownerId.equals(change.getAccount().getOwnerId())) {
            throw new UnauthorizedAccessException();
        }

        change.setAccount(accountRepository.findById(changeRequest.getAccountId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                        String.format("Account change with id %d not found", changeRequest.getAccountId()))
                ));
        change.setCategory(categoryRepository.findById(changeRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(
                                "Category of account change with id %d not found",
                                changeRequest.getCategoryId()
                        ))
                ));
        change.setTags(tagRepository.findByIdIn(changeRequest.getTagIds()));
        change.getAccount().setMoney(change.getAccount().getMoney() + change.getAmount());
        return converter.convertToResponse(entityRepository.save(change));
    }

    @Override
    public ChangeResponse update(ChangeRequest changeRequest, Long id) {
        if (id == null || id < 0 || changeRequest == null) {
            throw new IllegalArgumentException();
        }
        Change change = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Account change with id %d not found", id)
                )
        );
        Long ownerId = getAuthenticatedUserId();
        if (!ownerId.equals(change.getAccount().getOwnerId())) {
            throw new UnauthorizedAccessException();
        }
        Long oldAmount = change.getAmount();
        change.setAmount(changeRequest.getAmount());
        change.setDateTime(changeRequest.getDateTime());
        change.setPlace(changeRequest.getPlace());
        change.setComment(changeRequest.getComment());
        if (!changeRequest.getCategoryId().equals(change.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(changeRequest.getCategoryId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            String.format("Category of account change with id %d not found", id)
                    )
            );
            change.setCategory(newCategory);
        }
        change.setTags(tagRepository.findByIdIn(changeRequest.getTagIds()));
        change.getAccount().setMoney(change.getAccount().getMoney() - oldAmount + change.getAmount());
        return converter.convertToResponse(entityRepository.save(change));
    }

    @Override
    public void deleteById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Null instead of Account change id provided");
        }
        Change change = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Account change with id %d not found", id)
                )
        );
        Account account = change.getAccount();
        Long ownerId = getAuthenticatedUserId();
        if (!ownerId.equals(account.getOwnerId())) {
            throw new UnauthorizedAccessException();
        }
        account.setMoney(account.getMoney() - change.getAmount());
        accountRepository.save(account);
        entityRepository.deleteById(id);
    }

    @Override
    protected Long getEntityOwnerId(Change entity) {
        return entity.getAccount().getOwnerId();
    }
}
