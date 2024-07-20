package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.dto.TransactionBCARequest;
import org.k1.simplebankapp.dto.TransactionPendingResponse;
import org.k1.simplebankapp.dto.TransactionSuccessResponse;
import org.k1.simplebankapp.dto.ValidateTransactionRequest;
import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.Mutation;
import org.k1.simplebankapp.entity.Transaction;
import org.k1.simplebankapp.entity.User;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.k1.simplebankapp.entity.enums.TransactionStatus;
import org.k1.simplebankapp.entity.enums.TransactionType;
import org.k1.simplebankapp.mapper.TransactionMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.MutationRepository;
import org.k1.simplebankapp.repository.TransactionRepository;
import org.k1.simplebankapp.repository.UserRepository;
import org.k1.simplebankapp.service.TransactionService;
import org.k1.simplebankapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;

@Service
@Slf4j
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

    @Autowired
    private MutationRepository mutationRepository;

    @Override
    @Transactional
    public TransactionPendingResponse createTransaction(TransactionBCARequest request, Principal principal) {
        validationService.validate(request);
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not valid, please login again!");
        }
        Account sourceAccount = accountRepository.findFirstByNo(request.getAccountNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
        Account accountRecipient = accountRepository.findFirstByNo(request.getRecipientTargetAccount()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account recipient not found!"));
        if (sourceAccount.getBalance() < request.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance!");
        }
        Date dateNow = new Date();
        Transaction transaction = new Transaction();
        transaction.setId(Config.generateTransactionId() + transactionRepository.count() + 1);
        transaction.setAccount(sourceAccount);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setRecipientTargetAccount(accountRecipient);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionDate(dateNow);
        transaction.setTotal(request.getAmount());
        transaction.setBalanceAdd(false);
        transaction.setTimestamp(dateNow);
        transactionRepository.save(transaction);
        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionSuccessResponse validateTransaction(Principal principal, ValidateTransactionRequest request) {
        validationService.validate(request);
        userRepository.findFirstByUsernameAndPin(principal.getName(), request.getPin()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pin not valid, please try again!"));
        Transaction transaction = transactionRepository.findFirstByIdAndTransactionType(request.getTransactionId(), request.getTransactionType()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found!"));
        Account sourceAccount = accountRepository.findFirstByNo(transaction.getAccount().getNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
        Account accountRecipient = accountRepository.findFirstByNo(transaction.getRecipientTargetAccount().getNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account recipient not found!"));
        Date dateNow = new Date();
        sourceAccount.setBalance(sourceAccount.getBalance() - transaction.getAmount());
        accountRecipient.setBalance(accountRecipient.getBalance() + transaction.getAmount());
        accountRepository.save(sourceAccount);
        accountRepository.save(accountRecipient);

        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionDate(dateNow);
        transaction.setTimestamp(dateNow);

        transactionRepository.save(transaction);

        Mutation mutation = new Mutation();
        mutation.setTransaction(transaction);
        mutation.setMutationType(MutationType.PENGELUARAN);
        mutation.setTimestamp(dateNow);
        mutationRepository.save(mutation);
        return transactionMapper.toTransactionSuccessResponse(transaction);
    }
}
