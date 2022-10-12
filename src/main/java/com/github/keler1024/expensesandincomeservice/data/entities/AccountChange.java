package com.github.keler1024.expensesandincomeservice.data.entities;

import com.github.keler1024.expensesandincomeservice.data.enums.AccountChangeType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class AccountChange {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="account_id")
    private Account account;
    private AccountChangeType changeType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private AccountChangeCategory category;
    private Long amount;
    private LocalDateTime dateTime;
    private String place;
    private String comment;

    public AccountChange(Account account,
                         AccountChangeType changeType,
                         AccountChangeCategory category,
                         Long amount,
                         LocalDateTime dateTime,
                         String place,
                         String comment) {
        this.account = account;
        this.changeType = changeType;
        this.category = category;
        this.amount = amount;
        this.dateTime = dateTime;
        this.place = place;
        this.comment = comment;
    }

    public AccountChange(Long id,
                         Account account,
                         AccountChangeType changeType,
                         AccountChangeCategory category,
                         Long amount,
                         LocalDateTime dateTime,
                         String place,
                         String comment) {
        this.id = id;
        this.account = account;
        this.changeType = changeType;
        this.category = category;
        this.amount = amount;
        this.dateTime = dateTime;
        this.place = place;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountChangeCategory getCategory() {
        return category;
    }

    public void setCategory(AccountChangeCategory category) {
        this.category = category;
    }

    public AccountChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(AccountChangeType changeType) {
        this.changeType = changeType;
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

    public Account getAccountId() {
        return account;
    }

    public void setAccountId(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "AccountChange{" +
                "id=" + id +
                ", account=" + account +
                ", changeType=" + changeType +
                ", category=" + category +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", place='" + place + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
