package org.k1.simplebankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankTransferResponse {
    private String sourceFullName;
    private String sourceAccountNo;
    private String sourceBankName;
    private Double amount;
    private String transactionId;
    private String transactionType;
    private String recipientBankName;
    private String recipientBankAccountNo;
    private String recipientFullName;
    private String noRef;
    private String date;
}
