package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.ChangeRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import com.github.keler1024.expensesandincomeservice.repository.specification.SpecificationOnConjunctionBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BudgetConverter implements IRequestToEntityToResponseConverter<BudgetRequest, Budget, BudgetResponse>{
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ChangeRepository changeRepository;

    public BudgetConverter(CategoryRepository categoryRepository,
                           TagRepository tagRepository,
                           ChangeRepository changeRepository) {
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.changeRepository = changeRepository;
    }

    @Override
    public final Budget convertToEntity(final BudgetRequest budgetRequest) {
        if (budgetRequest == null || (budgetRequest.getCategoryId() == null && budgetRequest.getTagId() == null)) {
            throw new IllegalArgumentException();
        }
        Budget budget = new Budget();
        budget.setSize(budgetRequest.getSize());
        budget.setStartDate(budgetRequest.getStartDate());
        budget.setEndDate(budgetRequest.getEndDate());
        Category category = null;
        if (budgetRequest.getCategoryId() != null) {
            category = categoryRepository.findById(budgetRequest.getCategoryId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Category with id " + budgetRequest.getCategoryId() + " not found"
                    )
            );
        }
        Tag tag = null;
        if (budgetRequest.getTagId() != null) {
            tag = tagRepository.findById(budgetRequest.getTagId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Tag with id " + budgetRequest.getTagId() + " not found"
                    )
            );
        }
        budget.setCategory(category);
        budget.setTag(tag);
        return budget;
    }

    @Override
    public final BudgetResponse convertToResponse(final Budget budget) {
        if (budget == null) {
            throw new NullPointerException();
        }
        Long categoryId = budget.getCategory() == null ? null : budget.getCategory().getId();
        Long tagId = budget.getTag() == null ? null : budget.getTag().getId();
        Long spent = calculateBudgetSpending(budget);
        return new BudgetResponse(
                budget.getId(),
                budget.getSize(),
                budget.getStartDate(),
                budget.getEndDate(),
                categoryId,
                tagId,
                spent
        );
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
