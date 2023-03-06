package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.BudgetConverter;
import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import com.github.keler1024.expensesandincomeservice.repository.BudgetRepository;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.ChangeRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import com.github.keler1024.expensesandincomeservice.repository.specification.SpecificationOnConjunctionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BudgetService extends BaseService<BudgetRequest, Budget, BudgetResponse, BudgetRepository>{

    @Autowired
    public BudgetService(BudgetRepository budgetRepository, BudgetConverter budgetConverter) {
        super(budgetRepository, budgetConverter);
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
        return budgetList.stream().map(converter::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public BudgetResponse add(BudgetRequest budgetRequest) {
        if (budgetRequest == null) {
            throw new NullPointerException();
        }
        if (budgetRequest.getCategoryId() == null && budgetRequest.getTagId() == null
                || budgetRequest.getCategoryId() != null && budgetRequest.getTagId() != null) {
            throw new IllegalArgumentException("Budget request must provide either Category id or Tag id");
        }
        Budget newBudget = converter.convertToEntity(budgetRequest);
        Long ownerId = getAuthenticatedUserId();
        newBudget.setOwnerId(ownerId);
        newBudget = entityRepository.save(newBudget);
        return converter.convertToResponse(newBudget);
    }

    @Override
    public BudgetResponse update(BudgetRequest budgetRequest, Long id) {
        if (id == null || id < 0 || budgetRequest == null) {
            throw new IllegalArgumentException();
        }
        if (budgetRequest.getCategoryId() == null && budgetRequest.getTagId() == null
                || budgetRequest.getCategoryId() != null && budgetRequest.getTagId() != null) {
            throw new IllegalArgumentException("Budget request must provide either Category id or Tag id");
        }
        Budget budget = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Budget with id %d not found", id))
        );
        Long ownerId = getAuthenticatedUserId();
        if (!ownerId.equals(budget.getOwnerId())) {
            throw new UnauthorizedAccessException();
        }
        Budget newBudget = converter.convertToEntity(budgetRequest);
        budget.setSize(newBudget.getSize());
        budget.setStartDate(newBudget.getStartDate());
        budget.setEndDate(newBudget.getEndDate());
        budget.setCategory(newBudget.getCategory());
        budget.setTag(newBudget.getTag());
        budget = entityRepository.save(budget);
        return converter.convertToResponse(budget);
    }

    @Override
    protected Long getEntityOwnerId(Budget entity) {
        return entity.getOwnerId();
    }

}
