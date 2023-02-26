package com.github.keler1024.expensesandincomeservice.model.response;

import java.time.LocalDate;
import java.util.Objects;

public class BudgetResponse {
    private Long id;
    private Long size;
    private Long spent;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long categoryId;
    private Long tagId;

    public BudgetResponse(Long id,
                          Long size,
                          LocalDate startDate,
                          LocalDate endDate,
                          Long categoryId,
                          Long tagId) {
        this.id = id;
        this.size = size;
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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getSpent() {
        return spent;
    }

    public void setSpent(Long spent) {
        this.spent = spent;
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
                && Objects.equals(getSize(), that.getSize())
                && Objects.equals(getStartDate(), that.getStartDate())
                && Objects.equals(getEndDate(), that.getEndDate())
                && Objects.equals(getCategoryId(), that.getCategoryId())
                && Objects.equals(getTagId(), that.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getSize(),
                getStartDate(),
                getEndDate(),
                getCategoryId(),
                getTagId()
        );
    }
}
