package org.k1.simplebankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPendingResponse {
    private String transactionId;
    private String transactionType;
    private String fullNameAccount;
    private String fullNameRecipientAccount;
    private String noAccount;
    private String noAccountRecipient;
    private Double adminFee;
}
