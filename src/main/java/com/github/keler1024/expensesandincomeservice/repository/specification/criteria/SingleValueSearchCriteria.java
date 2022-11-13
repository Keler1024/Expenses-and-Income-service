package com.github.keler1024.expensesandincomeservice.repository.specification.criteria;

public class SingleValueSearchCriteria {
    private final String key;
    private final String operation;
    private final Object value;

    public SingleValueSearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }
}
