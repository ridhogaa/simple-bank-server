package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.config.EmailTemplate;
import org.k1.simplebankapp.dto.*;
import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.Merchant;
import org.k1.simplebankapp.entity.QrisPayment;
import org.k1.simplebankapp.entity.Transaction;
import org.k1.simplebankapp.entity.enums.TransactionStatus;
import org.k1.simplebankapp.entity.enums.TransactionType;
import org.k1.simplebankapp.mapper.QrisMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.MerchantRepository;
import org.k1.simplebankapp.repository.QrisPaymentRepository;
import org.k1.simplebankapp.repository.TransactionRepository;
import org.k1.simplebankapp.service.EmailService;
import org.k1.simplebankapp.service.QrisService;
import org.k1.simplebankapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class QrisServiceImpl implements QrisService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private QrisPaymentRepository qrisPaymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private QrisMapper qrisMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplate emailTemplate;

    @Override
    public QRCodeResponse generateQRCode(QRCodeRequest request, Principal principal) {
        validationService.validate(request);
        Account sourceAccount = validationService.validateCurrentUserHaveThisAccount(principal, request.getAccountNo());
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

        QrisPayment qrisPayment = saveQRCodeAccount(request.getAccountNo(), request.getAmount());
        return qrisMapper.toQRCodeResponse(qrisPayment);
    }

    @Override
    public ScanQrisResponse scanQRIS(String qrCode) {
        QrisPayment qrisPayment = qrisPaymentRepository.findByQrisCode(qrCode).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "QR code not found!"));

        if (qrCode.startsWith("ACC") && qrisPayment.getExpirationTime().isBefore(LocalDateTime.now())) {
            qrisPaymentRepository.delete(qrisPayment);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR code expired!");
        }

        if (qrCode.startsWith("ACC") && qrisPayment.getIsPaid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR code already paid!");
        }

        Account account = null;
        Merchant merchant = null;
        if (qrCode.startsWith("ACC"))
            account = accountRepository.findFirstByNo(qrisPayment.getAccountNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
        if (qrCode.startsWith("MRCHNT"))
            merchant = merchantRepository.findById(Long.valueOf(qrisPayment.getAccountNo())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found!"));

        String name = qrCode.startsWith("ACC") ? account.getUser().getFullname() : merchant.getName();
        return qrisMapper.toQRISReceiveFundsResponse(qrisPayment, name);
    }

    @Override
    @Transactional
    public QrisResponse confirmQRISReceives(ScanQrisReceiveRequest request, Principal principal) {
        QrisPayment qrisPayment = qrisPaymentRepository.findByQrisCode(request.getQrCode()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "QR code not found!"));
        if (qrisPayment.getIsPaid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR code already paid!");
        }
        if (request.getAccountNo().equalsIgnoreCase(qrisPayment.getAccountNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account number and recipient account number cannot be same!");
        }
        Account sourceAccount = validationService.validateCurrentUserHaveThisAccount(principal, request.getAccountNo());
        Account account = accountRepository.findFirstByNo(qrisPayment.getAccountNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));

        Transaction transaction = new Transaction();
        transaction.setId(Config.generateTransactionId());
        transaction.setAccount(account);
        transaction.setTransactionType(TransactionType.QRIS);
        transaction.setRecipientTargetAccount(sourceAccount.getNo());
        transaction.setRecipientTargetName(sourceAccount.getUser().getFullname());
        transaction.setRecipientTargetType(sourceAccount.getBank().getBankName());
        transaction.setAmount(qrisPayment.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setNoRef(Config.randomString(12, true));
        transaction.setFeeAdmin(sourceAccount.getBank().getAdminFee());

        sourceAccount.setBalance(sourceAccount.getBalance() + transaction.getAmount());
        account.setBalance(account.getBalance() - transaction.getAmount());

        accountRepository.save(sourceAccount);
        accountRepository.save(account);

        transaction.setAccount(account);
        transaction.setRecipientTargetAccount(sourceAccount.getNo());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCreatedDate(new Date());

        transactionRepository.save(transaction);

        qrisPayment.setIsPaid(true);
        qrisPaymentRepository.save(qrisPayment);

        emailService.sendAsync(sourceAccount.getUser().getEmail(), "Bukti Transaksi Berhasil", emailTemplate.createTransactionSuccessEmail(transaction));

        return qrisMapper.toQRISSuccessResponse(transaction);
    }

    @Override
    public QrisResponse confirmQRISMerchant(ScanQrisMerchantRequest request, Principal principal) {
        QrisPayment qrisPayment = qrisPaymentRepository.findByQrisCode(request.getQrCode()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "QR code not found!"));
        Account sourceAccount = validationService.validateCurrentUserHaveThisAccount(principal, request.getAccountNo());
        Merchant merchant = merchantRepository.findById(Long.valueOf(qrisPayment.getAccountNo())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found!"));
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
        transaction.setTransactionType(TransactionType.QRIS);
        transaction.setRecipientTargetAccount(merchant.getId().toString());
        transaction.setRecipientTargetName(merchant.getName());
        transaction.setRecipientTargetType("Merchant");
        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setNoRef(Config.randomString(12, true));
        transaction.setFeeAdmin(sourceAccount.getBank().getAdminFee());
        successTransactionQRISMerchant(sourceAccount, transaction, merchant);

        return qrisMapper.toQRISSuccessResponse(transaction);
    }

    @Override
    public ValidateQRCodeResponse validateQrCode(String qrCode) {
        QrisPayment qrisPayment = qrisPaymentRepository.findByQrisCode(qrCode).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "QR code not found!"));
        if (!qrCode.startsWith("ACC")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR code not valid!");
        }
        if (qrCode.startsWith("ACC") && qrisPayment.getExpirationTime().isBefore(LocalDateTime.now())) {
            qrisPaymentRepository.delete(qrisPayment);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR code expired!");
        }
        Account account = accountRepository.findFirstByNo(qrisPayment.getAccountNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));

        return qrisMapper.toValidateQRCodeResponse(qrisPayment, account.getUser().getFullname());
    }

    @Transactional
    public QrisPayment saveQRCodeAccount(String accountNo, Double amount) {
        QrisPayment qrisPayment = new QrisPayment();
        qrisPayment.setQrisCode("ACC" + Config.randomString(10, true));
        qrisPayment.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        qrisPayment.setIsPaid(false);
        qrisPayment.setAmount(amount);
        qrisPayment.setAccountNo(accountNo);
        qrisPaymentRepository.save(qrisPayment);
        return qrisPayment;
    }

    @Transactional
    public void successTransactionQRISMerchant(Account sourceAccount, Transaction transaction, Merchant accountRecipient) {
        sourceAccount.setBalance(sourceAccount.getBalance() - transaction.getAmount());
        accountRecipient.setBalance(accountRecipient.getBalance() + transaction.getAmount());

        accountRepository.save(sourceAccount);
        merchantRepository.save(accountRecipient);

        transaction.setAccount(sourceAccount);
        transaction.setRecipientTargetAccount(accountRecipient.getId().toString());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCreatedDate(new Date());
        transactionRepository.save(transaction);

        emailService.sendAsync(sourceAccount.getUser().getEmail(), "Bukti Transaksi Berhasil", emailTemplate.createTransactionSuccessEmail(transaction));
    }
}
