package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import org.springframework.stereotype.Component;

@Component
public class BudgetConverter implements IRequestToEntityToResponseConverter<BudgetRequest, Budget, BudgetResponse>{
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public BudgetConverter(CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

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

    public final BudgetResponse convertToResponse(final Budget budget) {
        if (budget == null) {
            throw new NullPointerException();
        }
        Long categoryId = budget.getCategory() == null ? null : budget.getCategory().getId();
        Long tagId = budget.getTag() == null ? null : budget.getTag().getId();
        return new BudgetResponse(
                budget.getId(),
                budget.getSize(),
                budget.getStartDate(),
                budget.getEndDate(),
                categoryId,
                tagId
        );
    }
}
