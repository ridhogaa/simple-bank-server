package org.k1.simplebankapp.serviceimpl.account;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.dto.account.AccountResponse;
import org.k1.simplebankapp.entity.User;
import org.k1.simplebankapp.mapper.AccountMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.UserRepository;
import org.k1.simplebankapp.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<AccountResponse> findAllByUser(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not valid, please login again!");
        }
        List<AccountResponse> accountResponseList = new ArrayList<>();
        accountRepository.findAllByUser(user).forEach(account -> {
            accountResponseList.add(accountMapper.toAccountResponse(account));
        });
        return accountResponseList;
    }
}
