package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.model.request.CategoryRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter
        implements IRequestToEntityToResponseConverter<CategoryRequest, Category, CategoryResponse> {

    @Override
    public Category convertToEntity(CategoryRequest request) {
        if (request == null) {
            throw new NullPointerException();
        }
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    @Override
    public CategoryResponse convertToResponse(Category entity) {
        if (entity == null) {
            throw new NullPointerException();
        }
        return new CategoryResponse(
                entity.getId(),
                entity.getName()
        );
    }
}
