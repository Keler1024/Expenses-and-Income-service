package com.github.keler1024.expensesandincomeservice.model.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface IRequestToEntityToResponseConverter<Q, E, S> {

    E convertToEntity(final Q request);

    S convertToResponse(final E entity);

    default List<E> createEntities(final Collection<Q> requests) {
        return requests.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    default List<S> createResponses(final Collection<E> entities) {
        return entities.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
}
