package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class BudgetConverter extends RequestToEntityToResponseConverter<BudgetRequest, Budget, BudgetResponse>{
    public BudgetConverter() {
        super(BudgetConverter::toEntity, BudgetConverter::toResponse);
    }

    private static Budget toEntity(@NonNull BudgetRequest budgetRequest) {
        Budget budget = new Budget();
        budget.setSize(budgetRequest.getSize());
        budget.setStartDate(budgetRequest.getStartDate());
        budget.setEndDate(budgetRequest.getEndDate());
        return budget;
    }

    private static BudgetResponse toResponse(Budget budget) {
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
