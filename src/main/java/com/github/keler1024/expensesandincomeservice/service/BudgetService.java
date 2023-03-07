package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.BudgetConverter;
import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import com.github.keler1024.expensesandincomeservice.repository.BudgetRepository;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService extends EntityService<BudgetRequest, Budget, BudgetResponse, BudgetRepository> {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Autowired
    public BudgetService(
            BudgetRepository budgetRepository,
            BudgetConverter budgetConverter,
            CategoryRepository categoryRepository,
            TagRepository tagRepository
    ) {
        super(budgetRepository, budgetConverter);
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public List<BudgetResponse> getByOwnerId(String relevance, LocalDate date) {
        if (relevance == null || (!relevance.equals("all") && date == null)) {
            throw new IllegalArgumentException();
        }
        Long ownerId = getAuthenticatedUserId();
        List<Budget> budgetList;
        switch (relevance) {
            case "all":
                budgetList = entityRepository.findByOwnerId(ownerId);
                break;
            case "current":
                budgetList = entityRepository.findByOwnerIdAndEndDateAfter(ownerId, date);
                break;
            case "past":
                budgetList = entityRepository.findByOwnerIdAndEndDateBefore(ownerId, date);
                break;
            default:
                throw new IllegalArgumentException("Unsupported relevance parameter value");
        }
        return converter.createResponses(budgetList);
    }

    @Override
    protected void performUpdate(Budget entity, BudgetRequest request) {
        if (request.getCategoryId() != null && request.getTagId() != null
                || request.getCategoryId() == null && request.getTagId() == null) {
            throw new IllegalArgumentException("Budget request provides both category and tag or none of them");
        }
        entity.setSize(request.getSize());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Category with id " + request.getCategoryId() + " not found"
                    )
            );
        }
        Tag tag = null;
        if (request.getTagId() != null) {
            tag = tagRepository.findById(request.getTagId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Tag with id " + request.getTagId() + " not found"
                    )
            );
        }
        entity.setCategory(category);
        entity.setTag(tag);
    }

    @Override
    protected void performOnAdd(Budget entity) {
        Long ownerId = getAuthenticatedUserId();
        entity.setOwnerId(ownerId);
    }

    @Override
    protected Long getEntityOwnerId(Budget entity) {
        return entity.getOwnerId();
    }

}
