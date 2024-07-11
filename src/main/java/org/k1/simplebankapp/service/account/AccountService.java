package org.k1.simplebankapp.service.account;

import org.k1.simplebankapp.dto.account.AccountResponse;

import java.security.Principal;
import java.util.List;

public interface AccountService {
    List<AccountResponse> findAllByUser(Principal principal);
}
