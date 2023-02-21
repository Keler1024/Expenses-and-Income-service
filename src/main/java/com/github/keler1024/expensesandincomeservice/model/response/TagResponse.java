package com.github.keler1024.expensesandincomeservice.model.response;

import java.util.Objects;

public class TagResponse {
    private Long id;
    private String name;

    public TagResponse() {}

    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof TagResponse)) return false;
        TagResponse that = (TagResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "TagResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
