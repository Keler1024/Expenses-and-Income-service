package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.TagConverter;
import com.github.keler1024.expensesandincomeservice.model.request.TagRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import com.github.keler1024.expensesandincomeservice.model.response.TagResponse;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService extends BaseService<TagRequest, Tag, TagResponse, TagRepository> {

    @Autowired
    public TagService(TagRepository tagRepository, TagConverter tagConverter) {
        super(tagRepository, tagConverter);
    }

    public List<TagResponse> getByOwnerId(Long ownerId) {
        if (ownerId == null || ownerId < 0) {
            throw new IllegalArgumentException();
        }
        return converter.createResponses(entityRepository.findByOwnerId(ownerId));
    }

    @Override
    public TagResponse add(TagRequest tagRequest, Long ownerId) {
        if (tagRequest == null) {
            throw new IllegalArgumentException();
        }
        Tag newTag = converter.convertToEntity(tagRequest);
        newTag.setOwnerId(ownerId);
        return converter.convertToResponse(entityRepository.save(newTag));
    }

    @Override
    public TagResponse update(TagRequest tagRequest, Long id, Long ownerId) {
        if (id == null || id < 0 || ownerId == null || ownerId < 0 || tagRequest == null) {
            throw new IllegalArgumentException();
        }
        Tag tag = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Tag with id %d not found", id))
        );

        if (!ownerId.equals(tag.getOwnerId())) {
            throw new UnauthorizedAccessException();
        }

        tag.setName(tagRequest.getName());
        return converter.convertToResponse(entityRepository.save(tag));
    }

    @Override
    protected Long getEntityOwnerId(Tag entity) {
        return entity.getOwnerId();
    }

}
