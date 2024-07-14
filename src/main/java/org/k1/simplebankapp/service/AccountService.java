package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.AccountResponse;

import java.security.Principal;
import java.util.List;

public interface AccountService {
    List<AccountResponse> findAllByUser(Principal principal);
}
