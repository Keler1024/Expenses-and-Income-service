package com.github.keler1024.expensesandincomeservice.repository;

import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeRepository extends JpaRepository<Change, Long>, JpaSpecificationExecutor<Change> {
    List<Change> findByAccount_Id(@NonNull Long id);
}
