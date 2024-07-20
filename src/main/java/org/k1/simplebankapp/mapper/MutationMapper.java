package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class MutationMapper {

    public MutationResponse toMutationResponse(Transaction transaction) {
        return MutationResponse.builder()
                .transactionType(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .date(transaction.getUpdatedDate().toString())
                .recipientTargetAccount(transaction.getRecipientTargetAccount().getNo())
                .transactionStatus(transaction.getStatus())
                .build();
    }
}
