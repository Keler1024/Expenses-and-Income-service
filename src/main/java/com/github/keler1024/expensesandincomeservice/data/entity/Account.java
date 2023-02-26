package com.github.keler1024.expensesandincomeservice.data.entity;

import com.github.keler1024.expensesandincomeservice.data.enums.Currency;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table
public class Account {
    @Id
    @SequenceGenerator(
            name = "account_sequence_generator",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence_generator"
    )
    private Long id;
    private Long ownerId;
    private Long money;
    private String name;
    private Currency currency;

    public Account(){}

    public Account(Long id, Long ownerId, Long money, String name, Currency currency) {
        this.id = id;
        this.ownerId = ownerId;
        this.money = money;
        this.name = name;
        this.currency = currency;
    }

    public Account(Long ownerId, Long money, String name, Currency currency) {
        this.ownerId = ownerId;
        this.money = money;
        this.name = name;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", money=" + money +
                ", name='" + name + '\'' +
                ", currency=" + currency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id)
                && Objects.equals(ownerId, account.ownerId)
                && Objects.equals(money, account.money)
                && Objects.equals(name, account.name)
                && currency == account.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, money, name, currency);
    }
}
