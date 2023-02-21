package com.github.keler1024.expensesandincomeservice.repository.specification;

import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

public class SpecificationOnConjunctionBuilder<E> {
    private Specification<E> specification;

    public SpecificationOnConjunctionBuilder() {
        specification = Specification.where(null);
    }

    public Specification<E> build() {
        return specification;
    }

    public SpecificationOnConjunctionBuilder<E> equal(String key, Object value) {
        specification = specification.and(new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(key), value);
            }
        });
        return this;
    }

    public SpecificationOnConjunctionBuilder<E> nestedEqual(List<String> keys, Object value) {
        specification = specification.and( new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<E> path = root;
                for (String key: keys) {
                    path = path.get(key);
                }
                return criteriaBuilder.equal(path, value);
            }
        });
        return this;
    }

    public SpecificationOnConjunctionBuilder<E> like(String key, String value) {
        specification = specification.and(new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get(key), value);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecificationOnConjunctionBuilder<E> between(String key, Y start, Y end) {
        specification = specification.and(new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get(key), start, end);
            }
        });
        return this;
    }

    public SpecificationOnConjunctionBuilder<E> containsAllTags(Set<Tag> tags) {
        specification = specification.and(new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate result = criteriaBuilder.and();
                for (Tag tag: tags) {
                    result = criteriaBuilder.and(result, criteriaBuilder.isMember(tag, root.get("tags")));
                }
                return result;
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecificationOnConjunctionBuilder<E> lessThan(String key, Y value) {
        specification = specification.and(new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThan(root.get(key), value);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecificationOnConjunctionBuilder<E> greaterThanOrEqualTo(String key, Y value) {
        specification = specification.and(new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(key), value);
            }
        });
        return this;
    }
}
