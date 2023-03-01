package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.model.request.TagRequest;
import com.github.keler1024.expensesandincomeservice.model.response.TagResponse;
import com.github.keler1024.expensesandincomeservice.security.AuthUtils;
import com.github.keler1024.expensesandincomeservice.service.TagService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/tag")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getByOwnerId(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if(!AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(tagService.getByOwnerId(ownerId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tagService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagResponse> add(
            @RequestBody TagRequest tagRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (tagRequest == null || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(tagService.add(tagRequest, ownerId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> update(
            @RequestBody TagRequest tagRequest,
            @PathVariable Long id) {
        if (tagRequest == null || id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tagService.update(tagRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TagResponse> deleteById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tagService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
