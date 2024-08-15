package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.config.Config;
import org.k1.simplebankapp.dto.QRCodeRequest;
import org.k1.simplebankapp.dto.QRCodeResponse;
import org.k1.simplebankapp.dto.ScanQRISRequest;
import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.QrisPayment;
import org.k1.simplebankapp.mapper.QrisMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.QrisPaymentRepository;
import org.k1.simplebankapp.service.QrisService;
import org.k1.simplebankapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;

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
    private QrisMapper qrisMapper;

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

        QrisPayment qrisPayment = saveQRCodeAccount(request.getAccountNo());
        return qrisMapper.toQRCodeResponse(qrisPayment);
    }

    @Override
    public Object scanQRIS(ScanQRISRequest request) {
        QrisPayment qrisPayment = qrisPaymentRepository.findByQrisCode(request.getQrCode()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "QR code not found!"));

        if (qrisPayment.getExpirationTime().isBefore(LocalDateTime.now())) {
            qrisPaymentRepository.delete(qrisPayment);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR code expired!");
        }

        if (request.getQrCode().startsWith("ACC")) {

        }

        qrisPayment.setPaid(true);
        qrisPaymentRepository.save(qrisPayment);
        return null;
    }

    @Transactional
    public QrisPayment saveQRCodeAccount(String accountNo) {
        QrisPayment qrisPayment = new QrisPayment();
        qrisPayment.setQrisCode("ACC" + Config.randomString(10, true));
        qrisPayment.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        qrisPayment.setPaid(false);
        qrisPayment.setAccountNo(accountNo);
        qrisPaymentRepository.save(qrisPayment);
        return qrisPayment;
    }
}
