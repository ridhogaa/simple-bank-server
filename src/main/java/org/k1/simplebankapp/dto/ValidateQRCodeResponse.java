package org.k1.simplebankapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateQRCodeResponse {
    private Boolean isPaid;
}
