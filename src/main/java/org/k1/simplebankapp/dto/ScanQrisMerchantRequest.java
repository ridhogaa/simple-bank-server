package org.k1.simplebankapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ScanQrisMerchantRequest {
    @NotBlank
    private String qrCode;
    @NotBlank
    private String accountNo;
    @NotNull
    private Double amount;
    @NotBlank
    private String pin;
}
