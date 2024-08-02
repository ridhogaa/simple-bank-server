package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.dto.BankTransferResponse;
import org.k1.simplebankapp.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BankTransferMapper {

    public BankTransferResponse toBankTransferResponse(Transaction transaction) {
        return BankTransferResponse.builder()
                .sourceFullName(transaction.getAccount().getUser().getFullname())
                .sourceBankName(transaction.getAccount().getBank().getBankName())
                .sourceAccountNo(transaction.getAccount().getNo())
                .amount(transaction.getAmount())
                .date(Config.convertToDateWIB(new Date()))
                .recipientBankAccountNo(transaction.getRecipientTargetAccount())
                .recipientBankName(transaction.getRecipientTargetType())
                .recipientFullName(transaction.getRecipientTargetName())
                .transactionId(transaction.getId())
                .transactionType(transaction.getTransactionType().name())
                .noRef(transaction.getNoRef())
                .build();
    }
}
