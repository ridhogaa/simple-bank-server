package org.k1.simplebankapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ScanQrisReceiveRequest {
    @NotBlank
    private String qrCode;
    @NotBlank
    private String accountNo;
}
