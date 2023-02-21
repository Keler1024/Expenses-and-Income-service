package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.data.entity.Category;
import com.github.keler1024.expensesandincomeservice.model.request.CategoryRequest;
import com.github.keler1024.expensesandincomeservice.model.response.CategoryResponse;
import com.github.keler1024.expensesandincomeservice.security.AuthenticationUtils;
import com.github.keler1024.expensesandincomeservice.security.JWTUtil;
import com.github.keler1024.expensesandincomeservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/v1/category")
public class CategoryController extends BaseController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getOwnerCategories(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (!AuthenticationUtils.isValidHeaderForBearerAuthentication(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = getUserIdFromAuthToken(authorization);
        List<CategoryResponse> result = categoryService.getDefaultCategories();
        result.addAll(categoryService.getCategoriesByOwnerId(ownerId));
//        if (result.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CategoryResponse result = categoryService.getCategory(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(
            @RequestBody CategoryRequest categoryRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (categoryRequest == null || !AuthenticationUtils.isValidHeaderForBearerAuthentication(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(
                categoryService.addCategory(categoryRequest, ownerId),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @RequestBody CategoryRequest categoryRequest,
            @PathVariable Long id
    ) {
        if (categoryRequest == null || id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(categoryService.updateCategory(categoryRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
