package org.k1.simplebankapp.serviceimpl;

import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.User;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.UserRepository;
import org.k1.simplebankapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.security.Principal;
import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void validate(Object request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Override
    public User validateCurrentUser(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not valid, please login again!");
        }
        return user;
    }

    @Override
    public Account validateCurrentUserHaveThisAccount(Principal principal, String accountNo) {
        User user = validateCurrentUser(principal);
        return accountRepository.findFirstByNoAndUser(accountNo, user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not valid or not found!"));
    }


}