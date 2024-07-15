package org.k1.simplebankapp.dto;

import lombok.Builder;
import lombok.Data;
import org.k1.simplebankapp.entity.enums.AccountType;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
public class AccountResponse {
    private String noAccount;
    private AccountType accountType;
    private String cardNumber;
    private Date expDate;
    private Double balance;
}
