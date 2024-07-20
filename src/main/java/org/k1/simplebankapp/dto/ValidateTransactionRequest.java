package org.k1.simplebankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.k1.simplebankapp.entity.enums.TransactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ValidateTransactionRequest {
    @NotBlank
    private String transactionId;
    @NotNull
    private TransactionType transactionType;
    @NotBlank
    private String pin;
}
