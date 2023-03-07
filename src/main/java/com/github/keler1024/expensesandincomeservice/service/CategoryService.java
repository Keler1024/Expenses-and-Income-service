package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.exception.UnauthorizedAccessException;
import com.github.keler1024.expensesandincomeservice.model.converter.CategoryConverter;
import com.github.keler1024.expensesandincomeservice.model.request.CategoryRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends EntityService<CategoryRequest, Category, CategoryResponse, CategoryRepository> {

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryConverter categoryConverter) {
        super(categoryRepository, categoryConverter);
    }

    public List<CategoryResponse> getByOwnerId() {
        Long ownerId = getAuthenticatedUserId();
        return converter.createResponses(entityRepository.findByOwnerId(ownerId));
    }

    public List<CategoryResponse> getDefaultCategories() {
        return converter.createResponses(entityRepository.findByOwnerIdNull());
    }

    @Override
    protected void performUpdate(Category entity, CategoryRequest request) {
        entity.setName(request.getName());
    }

    @Override
    protected void performOnAdd(Category entity) {
        Long ownerId = getAuthenticatedUserId();
        entity.setOwnerId(ownerId);
    }

    @Override
    protected Long getEntityOwnerId(Category entity) {
        return entity.getOwnerId();
    }
}
