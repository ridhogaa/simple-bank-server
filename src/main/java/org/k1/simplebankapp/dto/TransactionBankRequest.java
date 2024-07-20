package org.k1.simplebankapp.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class TransactionBankRequest {
    @NotBlank
    private String accountNo;
    @NotNull
    private Long recipientTargetAccountType;
    @NotBlank
    private String recipientTargetAccount;
    @NotNull
    private Double amount;
    private String description;
}
