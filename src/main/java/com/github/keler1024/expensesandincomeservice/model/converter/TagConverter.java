package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.model.request.CategoryRequest;
import com.github.keler1024.expensesandincomeservice.model.request.TagRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import com.github.keler1024.expensesandincomeservice.model.response.TagResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TagConverter implements IRequestToEntityToResponseConverter<TagRequest, Tag, TagResponse> {

    @Override
    public Tag convertToEntity(TagRequest request) {
        if (request == null) {
            throw new NullPointerException();
        }
        Tag tag = new Tag();
        tag.setName(request.getName());
        return tag;
    }

    @Override
    public TagResponse convertToResponse(Tag entity) {
        if (entity == null) {
            throw new NullPointerException();
        }
        return new TagResponse(
                entity.getId(),
                entity.getName()
        );
    }
}
