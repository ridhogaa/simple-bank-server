package org.k1.simplebankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.k1.simplebankapp.entity.enums.TransactionType;

import javax.validation.constraints.NotBlank;

@Data
public class ValidateTransactionRequest {
    @NotBlank
    private String transactionId;
    @NotBlank
    private TransactionType transactionType;
    @NotBlank
    private String pin;
}
