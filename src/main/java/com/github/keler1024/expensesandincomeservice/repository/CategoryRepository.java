package com.github.keler1024.expensesandincomeservice.repository;

import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByOwnerId(@NonNull Long ownerId);

    List<Category> findByOwnerIdNull();
}
