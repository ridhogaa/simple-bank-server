package org.k1.simplebankapp.service;

import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.User;

import java.security.Principal;

public interface ValidationService {
    void validate(Object request);

    User validateCurrentUser(Principal principal);

    Account validateCurrentUserHaveThisAccount(Principal principal, String accountNo);
}