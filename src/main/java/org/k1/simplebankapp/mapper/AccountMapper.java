package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.AccountResponse;
import org.k1.simplebankapp.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .noAccount(account.getNo())
                .accountType(account.getAccountType())
                .cardNumber(account.getCardNumber())
                .expDate(account.getExpDate())
                .balance(account.getBalance())
                .build();
    }
}
