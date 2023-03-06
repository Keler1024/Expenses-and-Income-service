package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.model.request.CategoryRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import com.github.keler1024.expensesandincomeservice.security.AuthUtils;
import com.github.keler1024.expensesandincomeservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getByOwnerId() {
        List<CategoryResponse> result = categoryService.getDefaultCategories();
        result.addAll(categoryService.getByOwnerId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CategoryResponse result = categoryService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> add(@RequestBody CategoryRequest categoryRequest) {
        if (categoryRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                categoryService.add(categoryRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @RequestBody CategoryRequest categoryRequest,
            @PathVariable Long id
    ) {
        if (categoryRequest == null || id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(categoryService.update(categoryRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        categoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
