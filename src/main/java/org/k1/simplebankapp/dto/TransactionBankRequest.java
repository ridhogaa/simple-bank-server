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
    @NotBlank
    private String recipientAccountNo;
    @NotBlank
    private String recipientBankName;
    @NotNull
    private Double amount;
    private String description;
}
