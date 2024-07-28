package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.dto.TransactionBankRequest;
import org.k1.simplebankapp.dto.TransactionPendingResponse;
import org.k1.simplebankapp.dto.TransactionSuccessResponse;
import org.k1.simplebankapp.dto.ValidateTransactionRequest;
import org.k1.simplebankapp.entity.*;
import org.k1.simplebankapp.entity.enums.TransactionStatus;
import org.k1.simplebankapp.entity.enums.TransactionType;
import org.k1.simplebankapp.mapper.TransactionMapper;
import org.k1.simplebankapp.repository.*;
import org.k1.simplebankapp.service.TransactionService;
import org.k1.simplebankapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Principal;

@Service
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private BankTransferRepository bankTransferRepository;

    @Override
    @Transactional
    public TransactionPendingResponse createTransaction(TransactionBankRequest request, Principal principal) {
        validationService.validate(request);
        Account sourceAccount = validationService.validateCurrentUserHaveThisAccount(principal, request.getAccountNo());
        BankTransfer accountRecipient = bankTransferRepository.findFirstByAccountAndRecipientAccountNo(sourceAccount, request.getRecipientAccountNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipient account not found!"));
        if (sourceAccount.getBalance() < request.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance!");
        }
        Transaction transaction = new Transaction();
        transaction.setId(Config.generateTransactionId());
        transaction.setAccount(sourceAccount);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setRecipientTargetAccount(accountRecipient.getRecipientAccountNo());
        transaction.setRecipientTargetName(accountRecipient.getRecipientName());
        transaction.setRecipientTargetType(accountRecipient.getBank().getBankName());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setNoRef(Config.randomString(12, true));
        transactionRepository.save(transaction);
        return transactionMapper.toTransactionResponse(transaction, accountRecipient);
    }

    @Override
    @Transactional
    public TransactionSuccessResponse validateTransaction(Principal principal, ValidateTransactionRequest request) {
        validationService.validate(request);

        Transaction transaction = transactionRepository.findFirstByIdAndTransactionType(request.getTransactionId(), request.getTransactionType())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found!"));
        if (transaction.getStatus() == TransactionStatus.SUCCESS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction already validated!");
        }

        Account sourceAccount = validationService.validateCurrentUserHaveThisAccount(principal, transaction.getAccount().getNo());

        if (!sourceAccount.getPin().equals(request.getPin())) {
            sourceAccount.setPinAttempts(sourceAccount.getPinAttempts() + 1);

            if (sourceAccount.getPinAttempts() >= 3) {
                accountRepository.save(sourceAccount);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is locked, please change pin!");
            }

            accountRepository.save(sourceAccount);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pin not valid, please try again!");
        }

        sourceAccount.setPinAttempts(0);

        BankTransfer accountRecipientBankTransfer = bankTransferRepository.findFirstByAccountAndRecipientAccountNo(sourceAccount, transaction.getRecipientTargetAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account recipient not found!"));
        Account accountRecipient = accountRepository.findFirstByNo(accountRecipientBankTransfer.getRecipientAccountNo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account recipient not found!"));

        sourceAccount.setBalance(sourceAccount.getBalance() - transaction.getAmount());
        accountRecipient.setBalance(accountRecipient.getBalance() + transaction.getAmount());

        accountRepository.save(sourceAccount);
        accountRepository.save(accountRecipient);

        transaction.setAccount(sourceAccount);
        transaction.setRecipientTargetAccount(accountRecipient.getNo());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);

        return transactionMapper.toTransactionSuccessResponse(transaction, accountRecipientBankTransfer);
    }


}
