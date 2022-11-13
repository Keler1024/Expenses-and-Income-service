package com.github.keler1024.expensesandincomeservice.repository.specification;

import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeTag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;


public class JPASpecifications {
    public static <T> Specification<T> equal(String key, Object value) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
    }

    public static <T> Specification<T> nestedEqual(List<String> keys, Object value) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<T> path = root;
                for (String key: keys) {
                    path = path.get(key);
                }
                return criteriaBuilder.equal(path, value);
            }
        };
    }

    public static <T> Specification<T> like(String key, String value) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get(key), value);
            }
        };
    }

    public static <T, Y extends Comparable<? super Y>> Specification<T> between(String key, Y start, Y end) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get(key), start, end);
            }
        };
    }

    public static <T> Specification<T> alwaysTrue() {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.and();
            }
        };
    }

    public static <T> Specification<T> containsAllTags(Set<AccountChangeTag> tags) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate result = criteriaBuilder.and();
                for (AccountChangeTag tag: tags) {
                    result = criteriaBuilder.and(result, criteriaBuilder.isMember(tag, root.get("tags")));
                }
                return result;
            }
        };
    }
}
