package com.github.keler1024.expensesandincomeservice.model.converter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RequestToEntityToResponseConverter<T, E, S> {

    private final Function<T, E> toEntity;
    private final Function<E, S> toResponse;

    public RequestToEntityToResponseConverter(final Function<T, E> toEntity, final Function<E, S> toResponse) {
        this.toEntity = toEntity;
        this.toResponse = toResponse;
    }

    public final E convertToEntity(final T request) {
        return toEntity.apply(request);
    }

    public final S convertToResponse(final E entity) {
        return toResponse.apply(entity);
    }

    public final List<E> createEntities(final Collection<T> requests) {
        return requests.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    public final List<S> createResponses(final Collection<E> entities) {
        return entities.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
}
