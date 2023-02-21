package com.github.keler1024.expensesandincomeservice.data.entity;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table
public class Category {
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
    private Long ownerId;

    public Category() {}

    public Category(Long id, String name, Long ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    public Category(String name, Long ownerId) {
        this.name = name;
        this.ownerId = ownerId;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "AccountChangeCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorId=" + ownerId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id)
                && Objects.equals(name, category.name)
                && Objects.equals(ownerId, category.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ownerId);
    }
}
