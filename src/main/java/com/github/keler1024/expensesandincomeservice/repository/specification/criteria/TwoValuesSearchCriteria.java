package com.github.keler1024.expensesandincomeservice.repository.specification.criteria;

public class TwoValuesSearchCriteria {
    private final String key;
    private final String operation;
    private final Object startValue;
    private final Object endValue;

    public TwoValuesSearchCriteria(String key, String operation, Object startValue, Object endValue) {
        this.key = key;
        this.operation = operation;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    public Object getStartValue() {
        return startValue;
    }

    public Object getEndValue() {
        return endValue;
    }
}
