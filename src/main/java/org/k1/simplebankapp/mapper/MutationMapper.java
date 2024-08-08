package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.entity.Transaction;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MutationMapper {

    public MutationResponse toMutationResponse(Transaction transaction, String noAccount) {
        return MutationResponse.builder()
                .transactionType(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .date(Config.convertToDateWIB(transaction.getCreatedDate()))
                .recipientTargetAccount(transaction.getRecipientTargetAccount())
                .transactionStatus(transaction.getStatus())
                .mutationType(noAccount.equals(transaction.getAccount().getNo()) ? MutationType.PENGELUARAN : MutationType.PEMASUKAN)
                .type(noAccount.equals(transaction.getAccount().getNo()) ? transaction.getRecipientTargetType() : transaction.getAccount().getBank().getBankName())
                .recipientName(noAccount.equals(transaction.getAccount().getNo()) ? transaction.getRecipientTargetName() : transaction.getAccount().getUser().getFullname())
                .build();
    }
}
