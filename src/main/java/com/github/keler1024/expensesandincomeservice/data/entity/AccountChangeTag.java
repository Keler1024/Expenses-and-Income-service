package com.github.keler1024.expensesandincomeservice.data.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class AccountChangeTag {
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
    private Long authorId;

    public AccountChangeTag() {
    }

    public AccountChangeTag(Long id, String name, Long authorId) {
        this.id = id;
        this.name = name;
        this.authorId = authorId;
    }

    public AccountChangeTag(String name, Long authorId) {
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
        return "AccountChangeTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorId=" + authorId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountChangeTag)) return false;
        AccountChangeTag that = (AccountChangeTag) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(authorId, that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authorId);
    }
}
