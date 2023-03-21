package com.github.keler1024.expensesandincomeservice.data.entity;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_sequence_generator")
    @SequenceGenerator(name = "tag_sequence_generator", sequenceName = "tag_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "owner_id", nullable = false)
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
        return "Tag{" +
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
