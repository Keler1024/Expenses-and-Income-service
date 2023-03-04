package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import com.github.keler1024.expensesandincomeservice.security.AuthUtils;
import com.github.keler1024.expensesandincomeservice.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path="api/v1/budget")
public class BudgetController {
    private final BudgetService budgetService;
    private static final Set<String> relevanceValues = Set.of("all", "current", "past");

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getByOwnerId(
            @RequestParam("relevance") String relevance,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (!relevanceValues.contains(relevance) || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return ResponseEntity.ok(budgetService.getByOwnerId(ownerId, relevance, date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getById(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (id == null || id < 0 || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return ResponseEntity.ok(budgetService.getById(id, ownerId));
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> add(
            @RequestBody BudgetRequest budgetRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (budgetRequest == null || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(budgetService.add(budgetRequest, ownerId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @RequestBody BudgetRequest budgetRequest,
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (budgetRequest == null || id == null || id < 0 || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(budgetService.update(budgetRequest, id, ownerId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetResponse> deleteById(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (id == null || id < 0 || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        budgetService.deleteById(id, ownerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
