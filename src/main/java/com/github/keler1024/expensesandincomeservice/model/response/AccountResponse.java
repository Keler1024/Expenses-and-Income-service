package com.github.keler1024.expensesandincomeservice.model.response;

import java.util.Objects;

public class AccountResponse {
    private Long id;
    private Long money;
    private String name;
    private String currency;

    public AccountResponse(){}

    public AccountResponse(Long id, Long money, String name, String currency) {
        this.id = id;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "id=" + id +
                ", money=" + money +
                ", name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountResponse)) return false;
        AccountResponse that = (AccountResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMoney(), that.getMoney()) && Objects.equals(getName(), that.getName()) && Objects.equals(getCurrency(), that.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMoney(), getName(), getCurrency());
    }
}
