package com.github.keler1024.expensesandincomeservice.model.request;

import java.time.LocalDate;
import java.util.Objects;

public class BudgetRequest {
    private Long size;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long categoryId;
    private Long tagId;

    public BudgetRequest(Long size,
                         LocalDate startDate,
                         LocalDate endDate,
                         Long categoryId,
                         Long tagId) {
        this.size = size;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = categoryId;
        this.tagId = tagId;
    }

    public BudgetRequest() {}

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
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
        if (!(o instanceof BudgetRequest)) return false;
        BudgetRequest that = (BudgetRequest) o;
        return Objects.equals(getSize(), that.getSize())
                && Objects.equals(getStartDate(), that.getStartDate())
                && Objects.equals(getEndDate(), that.getEndDate())
                && Objects.equals(getCategoryId(), that.getCategoryId())
                && Objects.equals(getTagId(), that.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getSize(),
                getStartDate(),
                getEndDate(),
                getCategoryId(),
                getTagId());
    }

    @Override
    public String toString() {
        return "BudgetRequest{" +
                "amount=" + size +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", categoryId=" + categoryId +
                ", tagId=" + tagId +
                '}';
    }
}
