package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.BankTransferRequest;
import org.k1.simplebankapp.dto.BankTransferResponse;

import java.security.Principal;
import java.util.List;

public interface BankTransferService {
    BankTransferResponse postListTransfer(BankTransferRequest request, Principal principal);

    List<BankTransferResponse> findAll(String noAccount, Principal principal);
}
