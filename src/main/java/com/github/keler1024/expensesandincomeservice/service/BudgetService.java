package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
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
import java.util.Objects;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetConverter budgetConverter;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository,
                         BudgetConverter budgetConverter,
                         CategoryRepository categoryRepository,
                         TagRepository tagRepository) {
        this.budgetRepository = budgetRepository;
        this.budgetConverter = budgetConverter;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public List<BudgetResponse> getAllBudgetsByOwnerId(Long ownerId) {
        if (ownerId == null || ownerId < 0) {
            throw new IllegalArgumentException();
        }
        return budgetConverter.createResponses(budgetRepository.findByOwnerId(ownerId));
    }

    public List<BudgetResponse> getPastBudgetsByOwnerId(Long ownerId, LocalDate date) {
        if (ownerId == null || ownerId < 0 || date == null) {
            throw new IllegalArgumentException();
        }
        return budgetConverter.createResponses(budgetRepository.findByOwnerIdAndEndDateBefore(ownerId, date));
    }

    public List<BudgetResponse> getCurrentBudgetsByOwnerId(Long ownerId, LocalDate date) {
        if (ownerId == null || ownerId < 0 || date == null) {
            throw new IllegalArgumentException();
        }
        return budgetConverter.createResponses(budgetRepository.findByOwnerIdAndEndDateAfter(ownerId, date));
    }

    public BudgetResponse getBudgetById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException();
        }
        return budgetConverter.convertToResponse(
                budgetRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Budget with id %d not found", id))
                ));
    }

    public BudgetResponse addBudget(BudgetRequest budgetRequest, Long ownerId) {
        if (budgetRequest == null || ownerId == null) {
            throw new NullPointerException();
        }
        if (budgetRequest.getCategoryId() == null && budgetRequest.getTagId() == null) {
            throw new IllegalArgumentException("Budget request must provide either Category id or Tag id");
        }
        Budget newBudget = budgetConverter.convertToEntity(budgetRequest);
        newBudget.setOwnerId(ownerId);
        if (budgetRequest.getCategoryId() != null) {
            newBudget.setCategory(categoryRepository.findById(budgetRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Category with id %d not found", budgetRequest.getCategoryId())
                            ))
            );
        }
        if (budgetRequest.getTagId() != null) {
            newBudget.setTag(tagRepository.findById(budgetRequest.getTagId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Tag with id %d not found", budgetRequest.getTagId())
                    ))
            );
        }
        return budgetConverter.convertToResponse(budgetRepository.save(newBudget));
    }

    public BudgetResponse updateBudget(BudgetRequest budgetRequest, Long id) {
        if (id == null || id < 0 || budgetRequest == null) {
            throw new IllegalArgumentException();
        }
        if (budgetRequest.getCategoryId() == null && budgetRequest.getTagId() == null) {
            throw new IllegalArgumentException("Budget request must provide either Category id or Tag id");
        }
        Budget budget = budgetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Budget with id %d not found", id))
        );
        budget.setAmount(budgetRequest.getAmount());
        budget.setStartDate(budgetRequest.getStartDate());
        budget.setEndDate(budgetRequest.getEndDate());
        if (!Objects.equals(budgetRequest.getCategoryId(), budget.getCategory().getId())) {
            if (budgetRequest.getCategoryId() == null) {
                budget.setCategory(null);
            }
            else {
                budget.setCategory(categoryRepository.findById(budgetRequest.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format("Category with id %d not found", budgetRequest.getCategoryId())))
                );
            }
        }
        if (!Objects.equals(budgetRequest.getTagId(), budget.getTag().getId())) {
            if (budgetRequest.getTagId() == null) {
                budget.setTag(null);
            }
            else {
                budget.setTag(tagRepository.findById(budgetRequest.getTagId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format("Tag with id %d not found", budgetRequest.getTagId())))
                );
            }
        }
        return budgetConverter.convertToResponse(budgetRepository.save(budget));
    }

    public void deleteBudgetById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException();
        }
        if (!budgetRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format("Budget with id %d not found", id));
        }
        budgetRepository.deleteById(id);
    }

}
