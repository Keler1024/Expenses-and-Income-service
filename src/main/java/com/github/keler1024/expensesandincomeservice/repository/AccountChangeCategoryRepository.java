package com.github.keler1024.expensesandincomeservice.repository;

import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountChangeCategoryRepository extends JpaRepository<AccountChangeCategory, Long> {
}
