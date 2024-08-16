package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.dto.QRCodeResponse;
import org.k1.simplebankapp.dto.ScanQrisResponse;
import org.k1.simplebankapp.dto.QrisResponse;
import org.k1.simplebankapp.dto.ValidateQRCodeResponse;
import org.k1.simplebankapp.entity.QrisPayment;
import org.k1.simplebankapp.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class QrisMapper {

    public QRCodeResponse toQRCodeResponse(QrisPayment qrisPayment) {
        return QRCodeResponse.builder()
                .qrCode(qrisPayment.getQrisCode())
                .dueDate(qrisPayment.getExpirationTime().toString())
                .build();
    }

    public ScanQrisResponse toQRISReceiveFundsResponse(QrisPayment qrisPayment, String senderName) {
        return ScanQrisResponse.builder()
                .senderName(senderName)
                .amount(qrisPayment.getAmount())
                .qrCode(qrisPayment.getQrisCode())
                .build();
    }

    public QrisResponse toQRISSuccessResponse(Transaction transaction) {
        return QrisResponse.builder()
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

    public ValidateQRCodeResponse toValidateQRCodeResponse(QrisPayment qrisPayment) {
        return ValidateQRCodeResponse.builder()
                .isPaid(qrisPayment.getIsPaid())
                .build();
    }
}
