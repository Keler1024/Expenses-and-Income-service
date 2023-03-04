package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.model.request.ChangeRequest;
import com.github.keler1024.expensesandincomeservice.model.response.ChangeResponse;
import com.github.keler1024.expensesandincomeservice.security.AuthUtils;
import com.github.keler1024.expensesandincomeservice.service.ChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping(path="api/v1/change")
public class ChangeController {

    private final ChangeService changeService;

    @Autowired
    public ChangeController(ChangeService changeService) {
        this.changeService = changeService;
    }


    @GetMapping
    public ResponseEntity<List<ChangeResponse>> getFilteredChanges(
            @RequestParam(name = "accountId", required = false) Long accountId,
            @RequestParam(name = "amount", required = false) Long amount,
            @RequestParam(name = "comparison", required = false) String comparison,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "place", required = false) String place,
            @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(name = "tags", required = false) Set<Long> tags,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (!AuthUtils.isValidBearerAuthHeader(authorization) || !datesAreValid(startDate, endDate)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        List<ChangeResponse> changeList = changeService.getAllChanges(
                ownerId, accountId, amount, comparison, categoryId, place, startDate, endDate, tags);
        return new ResponseEntity<>(changeList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChangeResponse> getById(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if(id == null || id < 0 || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        ChangeResponse changeResponse = changeService.getById(id, ownerId);
        return new ResponseEntity<>(changeResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ChangeResponse> post(
            @RequestBody ChangeRequest changeRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if(changeRequest == null || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(changeService.add(changeRequest, ownerId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChangeResponse> update(
            @RequestBody ChangeRequest changeRequest,
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if(id == null || id < 0 | changeRequest == null || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        return new ResponseEntity<>(changeService.update(changeRequest, id, ownerId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChangeResponse> deleteById(
            @PathVariable("id") Long id,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization
    ) {
        if (id == null || id < 0 || !AuthUtils.isValidBearerAuthHeader(authorization)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long ownerId = AuthUtils.getUserIdFromAuthToken(authorization);
        changeService.deleteById(id, ownerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static boolean datesAreValid(LocalDateTime start, LocalDateTime end) {
        return (start == null && end == null) || (start != null && !start.isBefore(end));
    }
}
