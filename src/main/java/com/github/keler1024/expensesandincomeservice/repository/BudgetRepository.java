package com.github.keler1024.expensesandincomeservice.repository;

import com.github.keler1024.expensesandincomeservice.data.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query("SELECT b FROM Budget b WHERE b.start_date <= :date and b.end_date >= :date")
    List<Budget> findByDateBetweenStartDateAndEndDate(@NonNull @Param("date") LocalDate date);

    List<Budget> findByEndDateBefore(@NonNull LocalDate endDate);

    List<Budget> findByOwnerId(@NonNull Long ownerId);
}
