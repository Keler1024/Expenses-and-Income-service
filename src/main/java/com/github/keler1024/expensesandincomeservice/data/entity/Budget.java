package com.github.keler1024.expensesandincomeservice.data.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "budget_sequence_generator")
    @SequenceGenerator(name = "budget_sequence_generator", sequenceName = "budget_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "amount", nullable = false)
    private Long amount;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
    @Column(name = "owner_id")
    private Long ownerId;

    public Budget(Long id,
                  Long amount,
                  LocalDate startDate,
                  LocalDate endDate,
                  Category category,
                  Tag tag,
                  Long ownerId) {
        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.tag = tag;
        this.ownerId = ownerId;
    }

    public Budget() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id=" + id +
                ", amount=" + amount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", category=" + category +
                ", tag=" + tag +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Budget)) return false;
        Budget budget = (Budget) o;
        return Objects.equals(getId(), budget.getId())
                && Objects.equals(getAmount(), budget.getAmount())
                && Objects.equals(getStartDate(), budget.getStartDate())
                && Objects.equals(getEndDate(), budget.getEndDate())
                && Objects.equals(getCategory(), budget.getCategory())
                && Objects.equals(getTag(), budget.getTag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAmount(), getStartDate(), getEndDate(), getCategory(), getTag());
    }
}
