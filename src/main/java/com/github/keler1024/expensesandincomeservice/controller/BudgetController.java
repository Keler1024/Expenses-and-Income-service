package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.model.request.BudgetRequest;
import com.github.keler1024.expensesandincomeservice.model.request.TagRequest;
import com.github.keler1024.expensesandincomeservice.model.response.BudgetResponse;
import com.github.keler1024.expensesandincomeservice.model.response.TagResponse;
import com.github.keler1024.expensesandincomeservice.security.AuthenticationUtils;
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
public class BudgetController extends BaseController {
    private final BudgetService budgetService;
    private static final Set<String> relevanceValues = Set.of("all", "current", "past");

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getOwnerBudgets(
            @RequestParam("relevance") String relevance,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (!relevanceValues.contains(relevance) || !AuthenticationUtils.isValidHeaderForBearerAuthentication(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = getUserIdFromAuthToken(authorization);
        return ResponseEntity.ok(budgetService.getBudgetsByOwnerId(ownerId, relevance, date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> add(
            @RequestBody BudgetRequest budgetRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (budgetRequest == null || !AuthenticationUtils.isValidHeaderForBearerAuthentication(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(budgetService.addBudget(budgetRequest, ownerId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @RequestBody BudgetRequest budgetRequest,
            @PathVariable Long id) {
        if (budgetRequest == null || id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(budgetService.updateBudget(budgetRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetResponse> delete(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        budgetService.deleteBudgetById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
