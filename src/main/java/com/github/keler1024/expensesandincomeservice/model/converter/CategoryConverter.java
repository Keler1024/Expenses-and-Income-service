package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.model.request.CategoryRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter extends RequestToEntityToResponseConverter<CategoryRequest, Category, CategoryResponse>{
    public CategoryConverter() {
        super(CategoryConverter::convertFromRequest, CategoryConverter::convertFromEntity);
    }

    private static Category convertFromRequest(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        return category;
    }

    private static CategoryResponse convertFromEntity(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
