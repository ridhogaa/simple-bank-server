package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.TransactionBankRequest;
import org.k1.simplebankapp.dto.TransactionPendingResponse;
import org.k1.simplebankapp.dto.TransactionSuccessResponse;
import org.k1.simplebankapp.dto.ValidateTransactionRequest;

import java.security.Principal;

public interface TransactionService {
    TransactionPendingResponse createTransaction(TransactionBankRequest request, Principal principal);
    TransactionSuccessResponse validateTransaction(Principal principal, ValidateTransactionRequest request);
}
