package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.TagConverter;
import com.github.keler1024.expensesandincomeservice.model.request.TagRequest;
import com.github.keler1024.expensesandincomeservice.model.response.TagResponse;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService extends EntityService<TagRequest, Tag, TagResponse, TagRepository> {

    @Autowired
    public TagService(TagRepository tagRepository, TagConverter tagConverter) {
        super(tagRepository, tagConverter);
    }

    public List<TagResponse> getByOwnerId() {
        Long ownerId = getAuthenticatedUserId();
        return converter.createResponses(entityRepository.findByOwnerId(ownerId));
    }

    @Override
    protected void performUpdate(Tag entity, TagRequest request) {
        entity.setName(request.getName());
    }

    @Override
    protected void performOnAdd(Tag entity) {
        Long ownerId = getAuthenticatedUserId();
        entity.setOwnerId(ownerId);
    }

    @Override
    protected Long getEntityOwnerId(Tag entity) {
        return entity.getOwnerId();
    }
}
