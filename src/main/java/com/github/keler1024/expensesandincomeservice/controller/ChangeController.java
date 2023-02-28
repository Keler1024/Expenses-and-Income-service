package com.github.keler1024.expensesandincomeservice.controller;

import com.github.keler1024.expensesandincomeservice.model.request.ChangeRequest;
import com.github.keler1024.expensesandincomeservice.model.response.ChangeResponse;
import com.github.keler1024.expensesandincomeservice.service.ChangeService;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam(name = "accountId") Long accountId,
            @RequestParam(name = "amount", required = false) Long amount,
            @RequestParam(name = "comparison", required = false) String comparison,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "place", required = false) String place,
//            @RequestParam(name = "comment", required = false) String comment,
            @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(name = "tags", required = false) Set<Long> tags) {
        if (accountId == null || !datesAreValid(startDate, endDate)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ChangeResponse> changeList = changeService.getAllChanges(
                accountId, amount, comparison, categoryId, place, startDate, endDate, tags);
        return new ResponseEntity<>(changeList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChangeResponse> getChange(@PathVariable Long id) {
        if(id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ChangeResponse changeResponse = changeService.getById(id);
        return new ResponseEntity<>(changeResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ChangeResponse> postChange(@RequestBody ChangeRequest changeRequest) {
        if(changeRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(changeService.add(changeRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChangeResponse> updateChange(@RequestBody ChangeRequest changeRequest, @PathVariable Long id) {
        if(id == null || id < 0 | changeRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(changeService.update(changeRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChangeResponse> deleteChange(@PathVariable("id") Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        changeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static boolean datesAreValid(LocalDateTime start, LocalDateTime end) {
        return (start == null && end == null) || (start != null && !start.isBefore(end));
    }
}
