package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.TransactionPendingResponse;
import org.k1.simplebankapp.dto.TransactionSuccessResponse;
import org.k1.simplebankapp.entity.BankTransfer;
import org.k1.simplebankapp.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionPendingResponse toTransactionResponse(Transaction transaction, BankTransfer bankTransfer) {
        return TransactionPendingResponse.builder()
                .transactionId(transaction.getId())
                .transactionType(transaction.getTransactionType().name())
                .fullNameAccount(transaction.getAccount().getUser().getFullname())
                .fullNameRecipientAccount(bankTransfer.getRecipientName())
                .noAccount(transaction.getAccount().getNo())
                .noAccountRecipient(bankTransfer.getRecipientAccountNo())
                .adminFee(bankTransfer.getBank().getAdminFee())
                .build();
    }

    public TransactionSuccessResponse toTransactionSuccessResponse(Transaction transaction, BankTransfer bankTransfer) {
        return TransactionSuccessResponse.builder()
                .fullName(transaction.getAccount().getUser().getFullname())
                .amount(transaction.getAmount())
                .date(transaction.getUpdatedDate())
                .recipientBankAccountNo(transaction.getRecipientTargetAccount())
                .recipientBankName(bankTransfer.getBank().getBankName())
                .transactionId(transaction.getId())
                .build();
    }
}
