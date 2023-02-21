package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.model.converter.TagConverter;
import com.github.keler1024.expensesandincomeservice.model.request.TagRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import com.github.keler1024.expensesandincomeservice.model.response.TagResponse;
import com.github.keler1024.expensesandincomeservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Autowired
    public TagService(TagRepository tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    public List<TagResponse> getByOwnerId(Long ownerId) {
        if (ownerId == null || ownerId < 0) {
            throw new IllegalArgumentException();
        }
        return tagConverter.createResponses(tagRepository.findByOwnerId(ownerId));
    }

    public TagResponse getById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException();
        }
        return tagConverter.convertToResponse(tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Tag with id %d not found", id))
        ));
    }

    public TagResponse add(TagRequest tagRequest, Long ownerId) {
        if (tagRequest == null) {
            throw new IllegalArgumentException();
        }
        Tag newTag = tagConverter.convertToEntity(tagRequest);
        newTag.setOwnerId(ownerId);
        return tagConverter.convertToResponse(tagRepository.save(newTag));
    }

    public TagResponse update(TagRequest tagRequest, Long id) {
        if (id == null || id < 0 || tagRequest == null) {
            throw new IllegalArgumentException();
        }
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Tag with id %d not found", id))
        );
        tag.setName(tagRequest.getName());
        return tagConverter.convertToResponse(tagRepository.save(tag));
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Null instead of Tag id provided");
        }
        if(!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag with id " + id + " not found");
        }
        tagRepository.deleteById(id);
    }
}
