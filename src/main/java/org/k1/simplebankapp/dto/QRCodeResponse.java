package org.k1.simplebankapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QRCodeResponse {
    private String qrCode;
    private String dueDate;
}
