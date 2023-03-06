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
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (!relevanceValues.contains(relevance)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(budgetService.getByOwnerId(relevance, date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(budgetService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> add(@RequestBody BudgetRequest budgetRequest) {
        if (budgetRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(budgetService.add(budgetRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @RequestBody BudgetRequest budgetRequest,
            @PathVariable Long id
    ) {
        if (budgetRequest == null || id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(budgetService.update(budgetRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetResponse> deleteById(
            @PathVariable Long id
    ) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        budgetService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
