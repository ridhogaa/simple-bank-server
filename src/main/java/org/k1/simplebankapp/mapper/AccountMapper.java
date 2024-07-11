package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.account.AccountResponse;
import org.k1.simplebankapp.dto.auth.LoginResponse;
import org.k1.simplebankapp.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class AccountMapper {
    public AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .noAccount(account.getNo())
                .type(account.getType())
                .cardNumber(account.getCardNumber())
                .expDate(account.getExpDate())
                .balance(account.getBalance())
                .build();
    }
}
