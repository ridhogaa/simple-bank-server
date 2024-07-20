package org.k1.simplebankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MutationResponse {
    private String date;
    private double amount;
    private String transactionType;
    private String recipientTargetAccount;
}
