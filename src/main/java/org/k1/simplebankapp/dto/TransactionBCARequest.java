package org.k1.simplebankapp.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class TransactionBCARequest {
    @NotBlank
    private String accountNo;
    @NotBlank
    private String recipientTargetAccountType;
    @NotBlank
    private String recipientTargetAccount;
    @NotBlank
    private Double amount;
    private String description;
}
