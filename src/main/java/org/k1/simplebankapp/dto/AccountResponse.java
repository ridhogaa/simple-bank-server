package org.k1.simplebankapp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class AccountResponse {
    private String noAccount;
    private String type;
    private String cardNumber;
    private String expDate;
    private BigInteger balance;
}
