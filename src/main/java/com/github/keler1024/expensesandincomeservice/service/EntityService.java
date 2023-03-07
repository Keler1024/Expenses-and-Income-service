package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.IRequestToEntityToResponseConverter;
import com.github.keler1024.expensesandincomeservice.security.AuthContext;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class EntityService<Q, E, S, R extends JpaRepository<E, Long>> {
    protected final R entityRepository;
    protected final IRequestToEntityToResponseConverter<Q, E, S> converter;

    public EntityService(R entityRepository, IRequestToEntityToResponseConverter<Q, E, S> converter) {
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
        checkOwnership(entity);

        return converter.convertToResponse(entity);
    }

    public void deleteById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Invalid resource id provided");
        }
        E entity = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource with id " + id + " not found")
        );
        checkOwnership(entity);

        entityRepository.deleteById(id);
    }

    protected final Long getAuthenticatedUserId() {
        return AuthContext.getUserId();
    }

    public S add(Q request) {
        if (request == null) {
            throw new NullPointerException("Null request dto provided");
        }
        E entity = converter.convertToEntity(request);
        performOnAdd(entity);
        return converter.convertToResponse(entityRepository.save(entity));
    }

    public S update(Q request, Long id) {
        if (id == null || id < 0 || request == null) {
            throw new IllegalArgumentException();
        }
        E entity = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource with id" + id + " not found")
        );
        checkOwnership(entity);
        performUpdate(entity, request);
        return converter.convertToResponse(entityRepository.save(entity));
    }

    protected void checkOwnership(E entity) {
        Long ownerId = getAuthenticatedUserId();
        if (!ownerId.equals(getEntityOwnerId(entity))) {
            throw new UnauthorizedAccessException();
        }
    }

    protected abstract void performUpdate(E entity, Q request);

    protected abstract void performOnAdd(E entity);

    protected abstract Long getEntityOwnerId(E entity);


}
