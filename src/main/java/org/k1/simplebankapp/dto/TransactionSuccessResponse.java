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
public class TransactionSuccessResponse {
    private String fullName;
    private Double amount;
    private String transactionId;
    private String recipientBankName;
    private String recipientBankAccountNo;
    private Date date;
}
