package com.github.keler1024.expensesandincomeservice.model.request;

import java.io.Serializable;
import java.util.Objects;

public class TagRequest implements Serializable {
    private String name;

    public TagRequest() {}

    public TagRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagRequest)) return false;
        TagRequest that = (TagRequest) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "TagRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
