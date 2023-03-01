package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.model.converter.IRequestToEntityToResponseConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<Q, E, S, R extends JpaRepository<E, Long>> {
    protected final R entityRepository;
    protected final IRequestToEntityToResponseConverter<Q, E, S> converter;

    public BaseService(R entityRepository, IRequestToEntityToResponseConverter<Q, E, S> converter) {
        this.entityRepository = entityRepository;
        this.converter = converter;
    }

    public S getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return converter.convertToResponse(
                entityRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Resource with id " + id + " not found")
                ));
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Null instead of Resource id provided");
        }
        if(!entityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource with id " + id + " not found");
        }
        entityRepository.deleteById(id);
    }

    public abstract S update(Q request, Long id);
}
