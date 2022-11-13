package com.github.keler1024.expensesandincomeservice.repository;

import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountChangeTagRepository extends JpaRepository<AccountChangeTag, Long> {
}
