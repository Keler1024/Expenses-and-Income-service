package com.github.keler1024.expensesandincomeservice.model.response;

import java.time.LocalDate;
import java.util.Objects;

public class BudgetResponse {
    private Long id;
    private Long amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long categoryId;
    private Long tagId;

    public BudgetResponse(Long id,
                          Long amount,
                          LocalDate startDate,
                          LocalDate endDate,
                          Long categoryId,
                          Long tagId) {
        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = categoryId;
        this.tagId = tagId;
    }

    public BudgetResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetResponse)) return false;
        BudgetResponse that = (BudgetResponse) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getAmount(), that.getAmount())
                && Objects.equals(getStartDate(), that.getStartDate())
                && Objects.equals(getEndDate(), that.getEndDate())
                && Objects.equals(getCategoryId(), that.getCategoryId())
                && Objects.equals(getTagId(), that.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getAmount(),
                getStartDate(),
                getEndDate(),
                getCategoryId(),
                getTagId()
        );
    }
}
