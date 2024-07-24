package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.dto.TransactionBankRequest;
import org.k1.simplebankapp.dto.TransactionPendingResponse;
import org.k1.simplebankapp.dto.TransactionSuccessResponse;
import org.k1.simplebankapp.dto.ValidateTransactionRequest;
import org.k1.simplebankapp.entity.*;
import org.k1.simplebankapp.entity.enums.MutationType;
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
    private UserRepository userRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private ValidationService validationService;

    @Override
    @Transactional
    public TransactionPendingResponse createTransaction(TransactionBankRequest request, Principal principal) {
        validationService.validate(request);
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not valid, please login again!");
        }
        Account sourceAccount = accountRepository.findFirstByNoAndUser(request.getAccountNo(), user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
        Account accountRecipient = accountRepository.findFirstByNoAndBankId(request.getRecipientTargetAccount(), request.getRecipientTargetAccountType()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account recipient not found!"));
        if (sourceAccount.getBalance() < request.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance!");
        }
        Transaction transaction = new Transaction();
        transaction.setId(Config.generateTransactionId() + transactionRepository.count() + 1);
        transaction.setAccount(sourceAccount);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setRecipientTargetAccount(accountRecipient);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);
        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionSuccessResponse validateTransaction(Principal principal, ValidateTransactionRequest request) {
        validationService.validate(request);

        User user = userRepository.findByUsername(principal.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not valid, please login again!");
        }

        // Check if the user has exceeded the PIN attempts
        if (user.getPinAttempts() >= 3) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is locked due to too many failed PIN attempts. Please try again later.");
        }

        // Validate the PIN
        userRepository.findFirstByUsernameAndPin(principal.getName(), request.getPin())
                .orElseThrow(() -> {
                    // Increment the PIN attempts
                    int newPinAttempts = user.getPinAttempts() + 1;
                    user.setPinAttempts(newPinAttempts);
                    userRepository.save(user);

                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pin not valid, please try again!");
                });

        // Reset the PIN attempts on successful validation
        user.setPinAttempts(0);
        userRepository.save(user);

        // Find the transaction
        Transaction transaction = transactionRepository.findFirstByIdAndTransactionType(request.getTransactionId(), request.getTransactionType())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found!"));

        // Check if the transaction is already validated
        if (transaction.getStatus().equals(TransactionStatus.SUCCESS)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction already validated!");
        }

        // Find the source and recipient accounts
        Account sourceAccount = accountRepository.findFirstByNo(transaction.getAccount().getNo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));

        Account accountRecipient = accountRepository.findFirstByNo(transaction.getRecipientTargetAccount().getNo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account recipient not found!"));

        // Update account balances
        sourceAccount.setBalance(sourceAccount.getBalance() - transaction.getAmount());
        accountRecipient.setBalance(accountRecipient.getBalance() + transaction.getAmount());
        accountRepository.save(sourceAccount);
        accountRepository.save(accountRecipient);

        // Update the transaction
        transaction.setAccount(sourceAccount);
        transaction.setRecipientTargetAccount(accountRecipient);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);

        return transactionMapper.toTransactionSuccessResponse(transaction);
    }

}
