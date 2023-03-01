package com.github.keler1024.expensesandincomeservice.model.converter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RequestToEntityToResponseConverter<Q, E, S> implements IRequestToEntityToResponseConverter<Q, E, S>{

    private final Function<Q, E> toEntity;
    private final Function<E, S> toResponse;

    public RequestToEntityToResponseConverter(final Function<Q, E> toEntity, final Function<E, S> toResponse) {
        this.toEntity = toEntity;
        this.toResponse = toResponse;
    }

    public final E convertToEntity(final Q request) {
        Objects.requireNonNull(request);
        return toEntity.apply(request);
    }

    public final S convertToResponse(final E entity) {
        Objects.requireNonNull(entity);
        return toResponse.apply(entity);
    }
}
