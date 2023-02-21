package com.github.keler1024.expensesandincomeservice.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.model.converter.AccountConverter;
import com.github.keler1024.expensesandincomeservice.model.request.AccountRequest;
import com.github.keler1024.expensesandincomeservice.model.response.AccountResponse;
import com.github.keler1024.expensesandincomeservice.security.AuthenticationUtils;
import com.github.keler1024.expensesandincomeservice.security.exception.JWTClaimMissingException;
import com.github.keler1024.expensesandincomeservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/v1/account")
public class AccountController extends BaseController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AccountResponse result = accountService.getAccount(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getOwnerAccounts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (!AuthenticationUtils.isValidHeaderForBearerAuthentication(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = getUserIdFromAuthToken(authorization);
        List<AccountResponse> result = accountService.getAccountsByOwnerId(ownerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> addAccount(@RequestBody AccountRequest accountRequest,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (accountRequest == null || !AuthenticationUtils.isValidHeaderForBearerAuthentication(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(
                accountService.addAccount(accountRequest, ownerId),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @RequestBody AccountRequest accountRequest,
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (accountRequest == null || id == null || id < 0
                || !AuthenticationUtils.isValidHeaderForBearerAuthentication(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(
                accountService.updateAccount(accountRequest, id),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Account> deleteAccount(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
