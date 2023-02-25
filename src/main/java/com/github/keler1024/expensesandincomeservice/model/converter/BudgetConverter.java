package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import org.springframework.lang.NonNull;

import java.util.function.Function;

public class BudgetConverter extends RequestToEntityToResponseConverter<BudgetRequest, Budget, BudgetResponse>{
    public BudgetConverter() {
        super(BudgetConverter::toEntity, BudgetConverter::toResponse);
    }

    private static Budget toEntity(@NonNull BudgetRequest budgetRequest) {
        Budget budget = new Budget();
        budget.setAmount(budgetRequest.getAmount());
        budget.setStartDate(budgetRequest.getStartDate());
        budget.setEndDate(budgetRequest.getEndDate());
        return budget;
    }

    private static BudgetResponse toResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getAmount(),
                budget.getStartDate(),
                budget.getEndDate(),
                budget.getCategory().getId(),
                budget.getTag().getId()
        );
    }
}
