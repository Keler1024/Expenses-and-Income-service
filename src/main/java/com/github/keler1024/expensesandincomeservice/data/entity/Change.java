package com.github.keler1024.expensesandincomeservice.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table
public class Change {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "change_sequence_generator")
    @SequenceGenerator(name = "change_sequence_generator", sequenceName = "change_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="account_id", referencedColumnName = "id")
    private Account account;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id", referencedColumnName = "id")
    private Category category;
    @Column(name = "amount", nullable = false)
    private Long amount;
    @Column(name = "datetime", nullable = false)
    private LocalDateTime dateTime;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_change_tags",
            joinColumns = @JoinColumn(name = "change_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags;
    @Column(name = "place", nullable = false)
    private String place;
    @Column(name = "comment", nullable = false)
    private String comment;

    public Change() {}

    public Change(Account account,
                  Category category,
                  Long amount,
                  LocalDateTime dateTime,
                  String place,
                  String comment,
                  Collection<Tag> tags) {
        this.account = account;
        this.category = category;
        this.amount = amount;
        this.dateTime = dateTime;
        this.place = place;
        this.comment = comment;
        this.tags = Set.copyOf(tags);
    }

    public Change(Long id,
                  Account account,
                  Category category,
                  Long amount,
                  LocalDateTime dateTime,
                  String place,
                  String comment,
                  Collection<Tag> tags) {
        this.id = id;
        this.account = account;
        this.category = category;
        this.amount = amount;
        this.dateTime = dateTime;
        this.place = place;
        this.comment = comment;
        this.tags = Set.copyOf(tags);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        if (tags == null) {
            throw new NullPointerException();
        }
        this.tags = new HashSet<>(tags);
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            throw new NullPointerException();
        }
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        if (tags == null) {
            throw new NullPointerException();
        }
        tags.remove(tag);
    }

    @Override
    public String toString() {
        return "AccountChange{" +
                "id=" + id +
                ", account=" + account +
//                ", changeType=" + changeType +
                ", category=" + category +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", place='" + place + '\'' +
                ", tags=" + tags +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Change)) return false;
        Change that = (Change) o;
        return Objects.equals(id, that.id)
                && Objects.equals(account, that.account)
//                && changeType == that.changeType
                && Objects.equals(category, that.category)
                && Objects.equals(tags, that.tags)
                && Objects.equals(amount, that.amount)
                && Objects.equals(dateTime, that.dateTime)
                && Objects.equals(place, that.place)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, category, tags, amount, dateTime, place, comment);
    }

}
