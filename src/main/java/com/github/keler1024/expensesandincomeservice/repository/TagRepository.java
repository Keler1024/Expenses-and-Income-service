package com.github.keler1024.expensesandincomeservice.repository;

import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Set<Tag> findByIdIn(@NonNull Collection<Long> ids);

    Set<Tag> findByOwnerId(Long ownerId);
}
