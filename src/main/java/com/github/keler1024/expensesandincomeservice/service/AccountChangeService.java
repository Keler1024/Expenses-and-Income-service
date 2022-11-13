package com.github.keler1024.expensesandincomeservice.service;

import com.github.keler1024.expensesandincomeservice.data.entity.AccountChange;
import com.github.keler1024.expensesandincomeservice.data.entity.AccountChangeTag;
import com.github.keler1024.expensesandincomeservice.data.enums.AccountChangeType;
import com.github.keler1024.expensesandincomeservice.exception.NotFoundInDatabaseException;
import com.github.keler1024.expensesandincomeservice.exception.NullArgumentServiceException;
import com.github.keler1024.expensesandincomeservice.repository.AccountChangeRepository;
import com.github.keler1024.expensesandincomeservice.repository.specification.JPASpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//TODO figure out null safety and exceptions
@Service
public class AccountChangeService {

    private final AccountChangeRepository accountChangeRepository;

    @Autowired
    public AccountChangeService(AccountChangeRepository accountChangeRepository) {
        this.accountChangeRepository = accountChangeRepository;
    }

    public List<AccountChange> getAllAccountChanges(
            Long accountId,
            Integer accountChangeType,
            Long categoryId,
            String place,
            String comment,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Set<AccountChangeTag> tags) {
        if (accountId == null) {
            throw new NullArgumentServiceException();
        }
        Specification<AccountChange> specification = JPASpecifications.nestedEqual(List.of("account","id"), accountId);
        if (accountChangeType != null) {
            specification = specification.and(JPASpecifications.equal("changeType", AccountChangeType.of(accountChangeType)));
        }
        if (categoryId != null) {
            specification = specification.and(JPASpecifications.nestedEqual(List.of("category","id"), categoryId));
        }
        if (place != null && !place.isEmpty()) {
            specification = specification.and(JPASpecifications.like("place", place));
        }
        if (comment != null && !comment.isEmpty()) {
            specification = specification.and(JPASpecifications.like("comment", comment));
        }
        if (startDate != null && endDate != null) {
            specification = specification.and(JPASpecifications.between("dateTime", startDate, endDate));
        }
        if (tags != null && !tags.isEmpty()) {
            specification = specification.and(JPASpecifications.containsAllTags(tags));
        }

        return accountChangeRepository.findAll(specification);
    }

    public Optional<AccountChange> getAccountChange(Long id) {
        if (id == null) {
            throw new NullArgumentServiceException();
        }
        return accountChangeRepository.findById(id);
    }

    public AccountChange addAccountChange(AccountChange accountChange) {
        return accountChangeRepository.save(accountChange);
    }

    public AccountChange updateAccountChange(AccountChange newAccountChange, Long id) {
        if (id == null || newAccountChange == null) {
            throw new NullArgumentServiceException();
        }
        return accountChangeRepository.findById(id)
                .map(accountChange -> {
                    accountChange.setAccount(newAccountChange.getAccount());
                    accountChange.setChangeType(newAccountChange.getChangeType());
                    accountChange.setAmount(newAccountChange.getAmount());
                    accountChange.setCategory(newAccountChange.getCategory());
                    accountChange.setDateTime(newAccountChange.getDateTime());
                    accountChange.setPlace(newAccountChange.getPlace());
                    accountChange.setComment(newAccountChange.getComment());
                    return accountChangeRepository.save(accountChange);
                })
                .orElseGet(() -> {  //TODO Decide if this is valid behavior, or exception should be thrown
                    newAccountChange.setId(id);
                    return accountChangeRepository.save(newAccountChange);
                });
    }

    public void deleteAccountChange(Long id) {
        if (id == null) {
            throw new NullArgumentServiceException("Null instead of AccountChange id provided");
        }
        if(!accountChangeRepository.existsById(id)) {
            throw new NotFoundInDatabaseException("AccountChange with id " + id + " not found in database");
        }
        accountChangeRepository.deleteById(id);
    }

    public List<AccountChange> getAccountChangesByAccountId(Long accountId) {
        return accountChangeRepository.findByAccount_Id(accountId);
    }
}
