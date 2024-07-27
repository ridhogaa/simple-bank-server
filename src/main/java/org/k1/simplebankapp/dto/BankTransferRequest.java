package org.k1.simplebankapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BankTransferRequest {

    @NotBlank
    private String accountNo;

    @NotBlank
    private String bankName;

    @NotBlank
    private String recipientAccountNo;

}
