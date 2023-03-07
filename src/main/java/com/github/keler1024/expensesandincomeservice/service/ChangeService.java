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
public class ChangeService extends EntityService<ChangeRequest, Change, ChangeResponse, ChangeRepository> {

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
    public void deleteById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Null instead of Account change id provided");
        }
        Change change = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Account change with id %d not found", id)
                )
        );
        checkOwnership(change);
        Account account = change.getAccount();
        account.setMoney(account.getMoney() - change.getAmount());
        accountRepository.save(account);
        entityRepository.deleteById(id);
    }

    @Override
    protected void performUpdate(Change entity, ChangeRequest request) {
        Long oldAmount = entity.getAmount();
        entity.setAmount(request.getAmount());
        entity.setDateTime(request.getDateTime());
        entity.setPlace(request.getPlace());
        entity.setComment(request.getComment());
        if (!request.getCategoryId().equals(entity.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Category of account change with id " + request.getCategoryId() + " not found"
                    )
            );
            entity.setCategory(newCategory);
        }
        entity.setTags(tagRepository.findByIdIn(request.getTagIds()));
        entity.getAccount().setMoney(entity.getAccount().getMoney() - oldAmount + entity.getAmount());
    }

    @Override
    protected void performOnAdd(Change entity) {
        entity.getAccount().setMoney(entity.getAccount().getMoney() + entity.getAmount());
    }

    @Override
    protected Long getEntityOwnerId(Change entity) {
        return entity.getAccount().getOwnerId();
    }
}
