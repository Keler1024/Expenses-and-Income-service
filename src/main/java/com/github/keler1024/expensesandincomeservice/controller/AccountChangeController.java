package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.data.entity.Account;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChange;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeCategory;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeTag;
import com.github.keler1024.expensesandincomeservice.service.AccountChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping(path="api/v1/accountchange")
public class AccountChangeController {

    private final AccountChangeService accountChangeService;

    @Autowired
    public AccountChangeController(AccountChangeService accountChangeService) {
        this.accountChangeService = accountChangeService;
    }

    //startDate and endDate included in date range
    //TODO Maybe add userId filtering
    @GetMapping
    public ResponseEntity<List<AccountChange>> getAllAccountChanges(
            @RequestParam(name = "accountId") Long accountId,
            @RequestParam(name = "type", required = false) Integer accountChangeType,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "place", required = false) String place,
            @RequestParam(name = "comment", required = false) String comment,
            @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(name = "tags", required = false) Set<AccountChangeTag> tags) {
        if (accountId == null || !datesAreValid(startDate, endDate)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<AccountChange> accountChangeList = accountChangeService.getAllAccountChanges(
                accountId, accountChangeType, categoryId, place, comment, startDate, endDate, tags);
        if (accountChangeList == null || accountChangeList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountChangeList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountChange> getAccountChange(@PathVariable Long id) {
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<AccountChange> accountChangeOptional;
        accountChangeOptional = accountChangeService.getAccountChange(id);

        if (accountChangeOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountChangeOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<AccountChange>> getUsersAccountChanges(@PathVariable("accountId") Long accountId) {
        if (accountId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<AccountChange> userAccountChanges = accountChangeService.getAccountChangesByAccountId(accountId);
        if (userAccountChanges == null || userAccountChanges.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userAccountChanges, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountChange> postAccountChange(@RequestBody AccountChange newAccountChange) {
        if(isBadRequestBody(newAccountChange)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(accountChangeService.addAccountChange(newAccountChange), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountChange> updateAccountChange(@RequestBody AccountChange newAccountChange, @PathVariable Long id) {
        if(id == null || isBadRequestBody(newAccountChange)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(accountChangeService.updateAccountChange(newAccountChange, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AccountChange> deleteAccountChange(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        accountChangeService.deleteAccountChange(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Subject to change
    private static boolean isBadRequestBody(AccountChange accountChange) {
        if (accountChange == null) {
            return true;
        }
        Account account = accountChange.getAccount();
        AccountChangeCategory category = accountChange.getCategory();
        return account == null
                || accountChange.getChangeType() == null
                || category == null
                || accountChange.getAmount() == null
                || accountChange.getAmount() < 0L
                || accountChange.getDateTime() == null
                || accountChange.getComment() == null
                || accountChange.getPlace() == null
                || account.getId() == null
                || account.getOwnerId() == null
                || account.getCurrency() == null
                || account.getName() == null
                || account.getMoney() == null
                || category.getId() == null
                || category.getAuthorId() == null
                || category.getName() == null;
    }

    private static boolean datesAreValid(LocalDateTime start, LocalDateTime end) {
        return start != null && end != null && !start.isAfter(end);
    }
}
