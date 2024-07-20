package org.k1.simplebankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.k1.simplebankapp.entity.enums.TransactionStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MutationResponse {
    private String date;
    private double amount;
    private String transactionType;
    private String recipientTargetAccount;
    private TransactionStatus transactionStatus;
}
