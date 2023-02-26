package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetConverter budgetConverter;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ChangeRepository changeRepository;
    private final Function<Budget, BudgetResponse> budgetMapper;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository,
                         BudgetConverter budgetConverter,
                         CategoryRepository categoryRepository,
                         TagRepository tagRepository,
                         ChangeRepository changeRepository) {
        this.budgetRepository = budgetRepository;
        this.budgetConverter = budgetConverter;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.changeRepository = changeRepository;
        this.budgetMapper = budget -> {
            BudgetResponse response = this.budgetConverter.convertToResponse(budget);
            response.setSpent(calculateBudgetSpending(budget));
            return response;
        };
    }

    public List<BudgetResponse> getAllBudgetsByOwnerId(Long ownerId) {
        if (ownerId == null || ownerId < 0) {
            throw new IllegalArgumentException();
        }
        List<Budget> budgetList = budgetRepository.findByOwnerId(ownerId);

        return budgetList.stream().map(budgetMapper).collect(Collectors.toList());
    }

    public List<BudgetResponse> getBudgetsByOwnerId(Long ownerId, String relevance, LocalDate date) {
        if (ownerId == null || ownerId < 0 || relevance == null || (!relevance.equals("all") && date == null)) {
            throw new IllegalArgumentException();
        }
        List<Budget> budgetList;
        switch (relevance) {
            case "all":
                budgetList = budgetRepository.findByOwnerId(ownerId);
                break;
            case "current":
                budgetList = budgetRepository.findByOwnerIdAndEndDateAfter(ownerId, date);
                break;
            case "past":
                budgetList = budgetRepository.findByOwnerIdAndEndDateBefore(ownerId, date);
                break;
            default:
                throw new IllegalArgumentException("Unsupported relevance parameter value");
        }
        return budgetList.stream().map(budgetMapper).collect(Collectors.toList());
    }

    public BudgetResponse getBudgetById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException();
        }
        Budget budget = budgetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Budget with id %d not found", id))
        );
        BudgetResponse response = budgetConverter.convertToResponse(budget);
        response.setSpent(calculateBudgetSpending(budget));
        return response;
    }

    public BudgetResponse addBudget(BudgetRequest budgetRequest, Long ownerId) {
        if (budgetRequest == null || ownerId == null) {
            throw new NullPointerException();
        }
        if (budgetRequest.getCategoryId() == null && budgetRequest.getTagId() == null
                || budgetRequest.getCategoryId() != null && budgetRequest.getTagId() != null) {
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
        newBudget = budgetRepository.save(newBudget);
        BudgetResponse response = budgetConverter.convertToResponse(newBudget);
        response.setSpent(calculateBudgetSpending(newBudget));
        return response;
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
        budget.setSize(budgetRequest.getSize());
        budget.setStartDate(budgetRequest.getStartDate());
        budget.setEndDate(budgetRequest.getEndDate());
        Long categoryId = budget.getCategory() == null ? null : budget.getCategory().getId();
        Long tagId = budget.getTag() == null ? null : budget.getTag().getId();
        if (!Objects.equals(budgetRequest.getCategoryId(), categoryId)) {
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
        if (!Objects.equals(budgetRequest.getTagId(), tagId)) {
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
        budget = budgetRepository.save(budget);
        BudgetResponse response = budgetConverter.convertToResponse(budget);
        response.setSpent(calculateBudgetSpending(budget));
        return response;
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

    private Long calculateBudgetSpending(Budget budget) {
        SpecificationOnConjunctionBuilder<Change> specificationBuilder = new SpecificationOnConjunctionBuilder<>();
        specificationBuilder.nestedEqual(List.of("account", "ownerId"), budget.getOwnerId());
        if (budget.getCategory() != null) {
            specificationBuilder.nestedEqual(List.of("category", "id"), budget.getCategory().getId());
        }
        if (budget.getTag() != null) {
            specificationBuilder.containsTag(budget.getTag());
        }
        if (budget.getStartDate() != null && budget.getEndDate() != null) {
            specificationBuilder.between("dateTime",
                    budget.getStartDate().atStartOfDay(), budget.getEndDate().atStartOfDay());
        }
        List<Change> changeList = changeRepository.findAll(specificationBuilder.build());
        return changeList.stream().mapToLong(Change::getAmount).sum();
    }

}
