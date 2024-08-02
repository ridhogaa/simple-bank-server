package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.*;

import java.security.Principal;

public interface BankTransferService {

    BankTransferResponse createTransaction(BankTransferRequest request, Principal principal);

}
