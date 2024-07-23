package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.entity.Transaction;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.springframework.stereotype.Component;

@Component
public class MutationMapper {

    public MutationResponse toMutationResponse(Transaction transaction, String noAccount) {
        return MutationResponse.builder()
                .transactionType(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .date(transaction.getUpdatedDate().toString())
                .recipientTargetAccount(transaction.getRecipientTargetAccount().getNo())
                .transactionStatus(transaction.getStatus())
                .mutationType(noAccount.equals(transaction.getAccount().getNo()) ? MutationType.PENGELUARAN : MutationType.PEMASUKAN)
                .type(noAccount.equals(transaction.getAccount().getNo()) ? transaction.getRecipientTargetAccount().getBank().getBankName() : transaction.getAccount().getBank().getBankName())
                .recipientName(noAccount.equals(transaction.getAccount().getNo()) ? transaction.getRecipientTargetAccount().getUser().getFullname() : transaction.getAccount().getUser().getFullname())
                .build();
    }
}
