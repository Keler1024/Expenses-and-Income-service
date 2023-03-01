package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.model.request.AccountRequest;
import com.github.keler1024.expensesandincomeservice.model.response.AccountResponse;
import com.github.keler1024.expensesandincomeservice.security.AuthUtils;
import com.github.keler1024.expensesandincomeservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AccountResponse result = accountService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getByOwnerId(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (!AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        List<AccountResponse> result = accountService.getByOwnerId(ownerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> add(@RequestBody AccountRequest accountRequest,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (accountRequest == null || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(
                accountService.add(accountRequest, ownerId),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(
            @RequestBody AccountRequest accountRequest,
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (accountRequest == null || id == null || id < 0
                || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                accountService.update(accountRequest, id),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Account> deleteById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        accountService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
