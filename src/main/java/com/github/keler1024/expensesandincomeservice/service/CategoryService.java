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
public class CategoryService extends BaseService<CategoryRequest, Category, CategoryResponse, CategoryRepository>{

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryConverter categoryConverter) {
        super(categoryRepository, categoryConverter);
    }

    public List<CategoryResponse> getByOwnerId(Long ownerId) {
        if (ownerId == null || ownerId < 0) {
            throw new IllegalArgumentException();
        }
        return converter.createResponses(entityRepository.findByOwnerId(ownerId));
    }

    public List<CategoryResponse> getDefaultCategories() {
        return converter.createResponses(entityRepository.findByOwnerIdNull());
    }

    @Override
    public CategoryResponse add(CategoryRequest categoryRequest, Long ownerId) {
        if (categoryRequest == null) {
            throw new IllegalArgumentException();
        }
        Category newCategory = converter.convertToEntity(categoryRequest);
        newCategory.setOwnerId(ownerId);
        return converter.convertToResponse(entityRepository.save(newCategory));
    }

    @Override
    public CategoryResponse update(CategoryRequest categoryRequest, Long id, Long ownerId) {
        if (id == null || id < 0 || categoryRequest == null) {
            throw new IllegalArgumentException();
        }
        Category category = entityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Category with id %d not found", id))
        );
        if (!ownerId.equals(category.getOwnerId())) {
            throw new UnauthorizedAccessException();
        }
        category.setName(categoryRequest.getName());
        return converter.convertToResponse(entityRepository.save(category));
    }

    @Override
    protected Long getEntityOwnerId(Category entity) {
        return entity.getOwnerId();
    }
}
