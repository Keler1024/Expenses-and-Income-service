package com.github.keler1024.expensesandincomeservice.data.entity;


import javax.persistence.*;
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
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public Budget(Long id, Long amount, LocalDateTime startDate, LocalDateTime endDate, Category category, Tag tag) {
        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.tag = tag;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
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
