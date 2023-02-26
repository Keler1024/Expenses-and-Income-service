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
    @Query("SELECT b FROM Budget b WHERE b.ownerId = :ownerId and b.endDate >= :date")
    List<Budget> findByOwnerIdAndEndDateAfter(
            @NonNull @Param("ownerId") Long ownerId,
            @NonNull @Param("date") LocalDate date
    );

    List<Budget> findByOwnerIdAndEndDateBefore(@NonNull Long ownerId, @NonNull LocalDate endDate);

    List<Budget> findByOwnerId(@NonNull Long ownerId);
}
