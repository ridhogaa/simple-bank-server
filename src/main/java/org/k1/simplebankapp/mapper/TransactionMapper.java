package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.TransactionPendingResponse;
import org.k1.simplebankapp.dto.TransactionSuccessResponse;
import org.k1.simplebankapp.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionPendingResponse toTransactionResponse(Transaction transaction) {
        return TransactionPendingResponse.builder()
                .transactionId(transaction.getId())
                .transactionType(transaction.getTransactionType().name())
                .build();
    }

    public TransactionSuccessResponse toTransactionSuccessResponse(Transaction transaction) {
        return TransactionSuccessResponse.builder()
                .fullName(transaction.getAccount().getUser().getFullname())
                .amount(transaction.getAmount())
                .build();
    }
}
