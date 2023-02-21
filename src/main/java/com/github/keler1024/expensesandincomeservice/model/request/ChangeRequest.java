package com.github.keler1024.expensesandincomeservice.model.request;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class ChangeRequest {
    private Long accountId;
//    private Integer changeType;
    private Long categoryId;
    private Long amount;
    private LocalDateTime dateTime;
    private Set<Long> tagIds;
    private String place;
    private String comment;

    public ChangeRequest() {}

    public ChangeRequest(Long accountId,
//                         Integer changeType,
                         Long categoryId,
                         Long amount,
                         LocalDateTime dateTime,
                         Set<Long> tagIds,
                         String place,
                         String comment
    ) {
        this.accountId = accountId;
//        this.changeType = changeType;
        this.categoryId = categoryId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.tagIds = tagIds;
        this.place = place;
        this.comment = comment;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

//    public Integer getChangeType() {
//        return changeType;
//    }

//    public void setChangeType(Integer changeType) {
//        this.changeType = changeType;
//    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "AccountChangeRequest{" +
                "accountId=" + accountId +
//                ", changeType=" + changeType +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", tagIds=" + tagIds +
                ", place='" + place + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeRequest)) return false;
        ChangeRequest that = (ChangeRequest) o;
        return Objects.equals(getAccountId(), that.getAccountId())
//                && Objects.equals(getChangeType(), that.getChangeType())
                && Objects.equals(getCategoryId(), that.getCategoryId())
                && Objects.equals(getAmount(), that.getAmount())
                && Objects.equals(getDateTime(), that.getDateTime())
                && Objects.equals(getTagIds(), that.getTagIds())
                && Objects.equals(getPlace(), that.getPlace())
                && Objects.equals(getComment(), that.getComment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getAccountId(),
//                getChangeType(),
                getCategoryId(),
                getAmount(),
                getDateTime(),
                getTagIds(),
                getPlace(),
                getComment());
    }
}
