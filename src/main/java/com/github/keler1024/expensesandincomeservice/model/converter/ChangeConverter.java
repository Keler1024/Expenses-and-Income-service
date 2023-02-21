package com.github.keler1024.expensesandincomeservice.model.converter;

import com.github.keler1024.expensesandincomeservice.data.entity.Change;
import com.github.keler1024.expensesandincomeservice.data.entity.Tag;
import com.github.keler1024.expensesandincomeservice.model.request.ChangeRequest;
import com.github.keler1024.expensesandincomeservice.model.response.ChangeResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ChangeConverter
        extends RequestToEntityToResponseConverter<ChangeRequest, Change, ChangeResponse> {

    public ChangeConverter() {
        super(ChangeConverter::convertFromRequest, ChangeConverter::convertFromEntity);
    }

    private static Change convertFromRequest(ChangeRequest changeRequest) {
        Change change = new Change();
//        change.setChangeType(ChangeType.of(changeRequest.getChangeType()));
        change.setAmount(changeRequest.getAmount());
        change.setDateTime(changeRequest.getDateTime());
        change.setPlace(changeRequest.getPlace());
        change.setComment(changeRequest.getComment());
        return change;
    }

    private static ChangeResponse convertFromEntity(Change change) {
        return new ChangeResponse(
                change.getId(),
                change.getAccount().getId(),
//                change.getChangeType().getCode(),
                change.getCategory().getId(),
                change.getAmount(),
                change.getDateTime(),
                change.getTags().stream().map(Tag::getId).collect(Collectors.toSet()),
                change.getPlace(),
                change.getComment()
        );
    }
}
