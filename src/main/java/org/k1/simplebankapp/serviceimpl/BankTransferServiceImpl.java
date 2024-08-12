package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.config.EmailTemplate;
import org.k1.simplebankapp.dto.*;
import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.Transaction;
import org.k1.simplebankapp.entity.enums.TransactionStatus;
import org.k1.simplebankapp.entity.enums.TransactionType;
import org.k1.simplebankapp.mapper.BankTransferMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.TransactionRepository;
import org.k1.simplebankapp.service.BankTransferService;
import org.k1.simplebankapp.service.EmailService;
import org.k1.simplebankapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BankTransferServiceImpl implements BankTransferService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankTransferMapper bankTransferMapper;

    @Autowired
    private EmailTemplate emailTemplate;

    @Autowired
    private EmailService emailService;

    @Override
    public BankTransferResponse createTransaction(BankTransferRequest request, Principal principal) {
        validationService.validate(request);
        if (request.getAccountNo().equalsIgnoreCase(request.getRecipientAccountNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account number and recipient account number cannot be same!");
        }

        if (!Config.isBankBCA(request.getRecipientBankName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank name not registered, please use BCA only");
        }

        log.info("request: {}", request);
        Account sourceAccount = validationService.validateCurrentUserHaveThisAccount(principal, request.getAccountNo());
        Account accountRecipient = accountRepository.findFirstByNo(request.getRecipientAccountNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipient account not found!"));
        if (sourceAccount.getBalance() < request.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance!");
        }

        if (sourceAccount.getPin().equals(request.getPin()) && sourceAccount.getPinAttempts() >= 3) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is locked, please change pin!");
        }

        if (!sourceAccount.getPin().equals(request.getPin())) {
            sourceAccount.setPinAttempts(sourceAccount.getPinAttempts() + 1);

            if (sourceAccount.getPinAttempts() >= 3) {
                accountRepository.save(sourceAccount);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is locked, please change pin!");
            }

            accountRepository.save(sourceAccount);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pin not valid, please try again!");
        }

        Transaction transaction = new Transaction();
        transaction.setId(Config.generateTransactionId());
        transaction.setAccount(sourceAccount);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setRecipientTargetAccount(accountRecipient.getNo());
        transaction.setRecipientTargetName(accountRecipient.getUser().getFullname());
        transaction.setRecipientTargetType(accountRecipient.getBank().getBankName());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setNoRef(Config.randomString(12, true));
        transaction.setFeeAdmin(accountRecipient.getBank().getAdminFee());

        successTransaction(sourceAccount, transaction);
        return bankTransferMapper.toBankTransferResponse(transaction);
    }

    @Transactional
    public void successTransaction(Account sourceAccount, Transaction transaction) {
        sourceAccount.setPinAttempts(0);

        Account accountRecipient = accountRepository.findFirstByNo(transaction.getRecipientTargetAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account recipient not found!"));

        sourceAccount.setBalance(sourceAccount.getBalance() - transaction.getAmount());
        accountRecipient.setBalance(accountRecipient.getBalance() + transaction.getAmount());

        accountRepository.save(sourceAccount);
        accountRepository.save(accountRecipient);

        transaction.setAccount(sourceAccount);
        transaction.setRecipientTargetAccount(accountRecipient.getNo());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCreatedDate(new Date());
        transactionRepository.save(transaction);

        emailService.sendAsync(sourceAccount.getUser().getEmail(), "Bukti Transaksi Berhasil", emailTemplate.createTransactionSuccessEmail(transaction));
    }
}
