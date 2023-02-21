package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
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
public class ChangeService {

    private final ChangeRepository changeRepository;
    private final ChangeConverter changeConverter;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    private static final List<String> comparisons = List.of("less", "more");

    @Autowired
    public ChangeService(
            ChangeRepository changeRepository,
            ChangeConverter changeConverter,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository
    ) {
        this.changeRepository = changeRepository;
        this.changeConverter = changeConverter;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public List<ChangeResponse> getAllAccountChanges(
            Long accountId,
            Long amount,
            String comparison,
            Long categoryId,
            String place,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Set<Long> tagIds) {
        if (accountId == null) {
            throw new IllegalArgumentException();
        }
        SpecificationOnConjunctionBuilder<Change> specificationBuilder = new SpecificationOnConjunctionBuilder<>();
        specificationBuilder.nestedEqual(List.of("account","id"), accountId);
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
            specificationBuilder.nestedEqual(List.of("category","id"), categoryId);
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
        return changeConverter.createResponses(changeRepository.findAll(specificationBuilder.build()));
    }

    public ChangeResponse getAccountChange(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException();
        }
        return changeConverter.convertToResponse(
                changeRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Account change with id %d not found", id)))
        );
    }

    public ChangeResponse addChange(ChangeRequest changeRequest) {
        if (changeRequest == null) {
            throw new IllegalArgumentException();
        }
        Change change = changeConverter.convertToEntity(changeRequest);
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
        ChangeResponse result = changeConverter.convertToResponse(changeRepository.save(change));
        return result;
    }

    public ChangeResponse updateAccountChange(ChangeRequest changeRequest, Long id) {
        if (id == null || id < 0 || changeRequest == null) {
            throw new IllegalArgumentException();
        }
        Change change = changeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Account change with id %d not found", id)
                )
        );
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
        return changeConverter.convertToResponse(changeRepository.save(change));
    }

    public void deleteAccountChange(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Null instead of Account change id provided");
        }
        if(!changeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account change with id " + id + " not found in database");
        }
        changeRepository.deleteById(id);
    }

    public List<ChangeResponse> getByAccountId(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Null instead of Account change id provided");
        }
        return changeConverter.createResponses(changeRepository.findByAccount_Id(accountId));
    }
}
