package com.github.keler1024.expensesandincomeservice.model.request;

import com.github.keler1024.expensesandincomeservice.data.enums.Currency;

import java.util.Objects;

public class AccountRequest {
    private Long money;
    private String name;
    private String currency;

    public AccountRequest(){}

    public AccountRequest(Long money, String name, String currency) {
        this.money = money;
        this.name = name;
        this.currency = currency;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "CreateAccountRequest{" +
                "money=" + money +
                ", name='" + name + '\'' +
                ", currency=" + currency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountRequest)) return false;
        AccountRequest that = (AccountRequest) o;
        return Objects.equals(getMoney(), that.getMoney())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getCurrency(), that.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMoney(), getName(), getCurrency());
    }
}
