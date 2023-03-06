package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.IRequestToEntityToResponseConverter;
import com.github.keler1024.expensesandincomeservice.security.AuthContext;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<Q, E, S, R extends JpaRepository<E, Long>> {
    protected final R entityRepository;
    protected final IRequestToEntityToResponseConverter<Q, E, S> converter;

    public BaseService(R entityRepository, IRequestToEntityToResponseConverter<Q, E, S> converter) {
        this.entityRepository = entityRepository;
        this.converter = converter;
    }

    public S getById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Invalid resource id provided");
        }
        E entity = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource with id " + id + " not found")
        );
        Long ownerId = getAuthenticatedUserId();
        if (!ownerId.equals(getEntityOwnerId(entity))) {
            throw new UnauthorizedAccessException();
        }

        return converter.convertToResponse(entity);
    }

    public void deleteById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Invalid resource id provided");
        }
        E entity = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource with id " + id + " not found")
        );
        Long ownerId = getAuthenticatedUserId();
        if (!ownerId.equals(getEntityOwnerId(entity))) {
            throw new UnauthorizedAccessException();
        }

        entityRepository.deleteById(id);
    }

    protected final Long getAuthenticatedUserId() {
        return AuthContext.getUserId();
    }

    public abstract S add(Q request);

    public abstract S update(Q request, Long id);

    protected abstract Long getEntityOwnerId(E entity);
}
