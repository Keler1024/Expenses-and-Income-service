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
public class TagConverter extends RequestToEntityToResponseConverter<TagRequest, Tag, TagResponse> {
    public TagConverter() {
        super(TagConverter::convertFromRequest, TagConverter::convertFromEntity);
    }

    private static Tag convertFromRequest(TagRequest tagRequest) {
        Tag tag = new Tag();
        tag.setName(tagRequest.getName());
        return tag;
    }

    private static TagResponse convertFromEntity(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName()
        );
    }
}
