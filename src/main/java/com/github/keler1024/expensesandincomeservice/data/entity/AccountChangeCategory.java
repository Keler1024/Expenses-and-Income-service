package com.github.keler1024.expensesandincomeservice.data.entity;

import javax.persistence.*;
import java.util.Objects;

//TODO Try to add null checks to keep it null safe
@Entity
@Table
public class AccountChangeCategory {
    @Id
    @SequenceGenerator(
            name = "accountChangeCategory_sequence_generator",
            sequenceName = "accountChangeCategory_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "accountChangeCategory_sequence_generator"
    )
    private Long id;
    private String name;
    private Long authorId;

    public AccountChangeCategory() {}

    public AccountChangeCategory(Long id, String name, Long authorId) {
        this.id = id;
        this.name = name;
        this.authorId = authorId;
    }

    public AccountChangeCategory(String name, Long authorId) {
        this.name = name;
        this.authorId = authorId;
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "AccountChangeCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorId=" + authorId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountChangeCategory)) return false;
        AccountChangeCategory category = (AccountChangeCategory) o;
        return Objects.equals(id, category.id)
                && Objects.equals(name, category.name)
                && Objects.equals(authorId, category.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authorId);
    }
}
