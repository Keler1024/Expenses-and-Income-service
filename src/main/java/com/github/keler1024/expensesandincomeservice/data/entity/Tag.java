package com.github.keler1024.expensesandincomeservice.data.entity;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table
public class Tag {
    @Id
    @SequenceGenerator(
            name = "accountChangeTag_sequence_generator",
            sequenceName = "accountChangeTag_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "accountChangeTag_sequence_generator"
    )
    private Long id;
    private String name;
    private Long ownerId;

    public Tag() {
    }

    public Tag(Long id, String name, Long ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    public Tag(String name, Long ownerId) {
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
        return "AccountChangeTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorId=" + ownerId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag that = (Tag) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ownerId);
    }
}
