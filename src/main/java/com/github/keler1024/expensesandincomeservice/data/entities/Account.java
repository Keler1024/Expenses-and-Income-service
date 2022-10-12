package com.github.keler1024.expensesandincomeservice.data.entities;

import com.github.keler1024.expensesandincomeservice.data.enums.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private Long money;
    private String name;
    private Currency currency;

    public Account(Long id, Long money, String name, Currency currency) {
        this.id = id;
        this.money = money;
        this.name = name;
        this.currency = currency;
    }

    public Account(Long money, String name, Currency currency) {
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
                ", money=" + money +
                ", name='" + name + '\'' +
                ", currency=" + currency +
                '}';
    }
}
