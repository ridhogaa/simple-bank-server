package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.*;

import java.security.Principal;
import java.util.List;

public interface BankTransferService {

    TransactionPendingResponse createTransaction(TransactionBankRequest request, Principal principal);

    TransactionSuccessResponse validateTransaction(Principal principal, ValidateTransactionRequest request);

}
