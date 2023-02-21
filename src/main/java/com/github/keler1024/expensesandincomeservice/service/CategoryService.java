package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.exception.ResourceNotFoundException;
import com.github.keler1024.expensesandincomeservice.model.converter.CategoryConverter;
import com.github.keler1024.expensesandincomeservice.model.request.CategoryRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import com.github.keler1024.expensesandincomeservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryConverter categoryConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
    }

    public List<CategoryResponse> getCategoriesByOwnerId(Long ownerId) {
        if (ownerId == null || ownerId < 0) {
            throw new IllegalArgumentException();
        }
        return categoryConverter.createResponses(categoryRepository.findByOwnerId(ownerId));
    }

    public List<CategoryResponse> getDefaultCategories() {
        return categoryConverter.createResponses(categoryRepository.findByOwnerIdNull());
    }

    public CategoryResponse getCategory(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException();
        }
        return categoryConverter.convertToResponse(
                categoryRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Category with id %d not found", id))
                ));
    }

    public CategoryResponse addCategory(CategoryRequest categoryRequest, Long ownerId) {
        if (categoryRequest == null) {
            throw new IllegalArgumentException();
        }
        Category newCategory = categoryConverter.convertToEntity(categoryRequest);
        newCategory.setOwnerId(ownerId);
        return categoryConverter.convertToResponse(categoryRepository.save(newCategory));
    }

    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id) {
        if (id == null || id < 0 || categoryRequest == null) {
            throw new IllegalArgumentException();
        }
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Category with id %d not found", id))
        );
        category.setName(categoryRequest.getName());
        return categoryConverter.convertToResponse(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException();
        }
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format("Category with id %d not found", id));
        }
        categoryRepository.deleteById(id);
    }
}
