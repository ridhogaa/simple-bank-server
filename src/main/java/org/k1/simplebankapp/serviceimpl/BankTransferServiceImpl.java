package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.dto.BankTransferRequest;
import org.k1.simplebankapp.dto.BankTransferResponse;
import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.BankTransfer;
import org.k1.simplebankapp.entity.User;
import org.k1.simplebankapp.mapper.BankTransferMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.BankTransferRepository;
import org.k1.simplebankapp.repository.UserRepository;
import org.k1.simplebankapp.service.BankTransferService;
import org.k1.simplebankapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BankTransferServiceImpl implements BankTransferService {

    @Autowired
    private BankTransferRepository bankTransferRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankTransferMapper bankTransferMapper;

    @Override
    @Transactional
    public BankTransferResponse postListTransfer(BankTransferRequest request, Principal principal) {
        validationService.validate(request);
        if (request.getAccountNo().equalsIgnoreCase(request.getRecipientAccountNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account number and recipient account number cannot be same!");
        }
        log.info("request: {}", request);
        Account sourceAccount = validationService.validateCurrentUserHaveThisAccount(principal, request.getAccountNo());
        if (bankTransferRepository.existsByAccountAndRecipientAccountNo(sourceAccount, request.getRecipientAccountNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank transfer already exists!");
        }
        if (request.getBankName().equalsIgnoreCase("BCA")) {
            Account accountRecipient = accountRepository.findFirstByNo(request.getRecipientAccountNo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account recipient not found!"));
            BankTransfer bankTransfer = new BankTransfer();
            bankTransfer.setBank(accountRecipient.getBank());
            bankTransfer.setAccount(sourceAccount);
            bankTransfer.setRecipientAccountNo(accountRecipient.getNo());
            bankTransfer.setRecipientName(accountRecipient.getUser().getFullname());
            bankTransferRepository.save(bankTransfer);
            return bankTransferMapper.toBankTransferResponse(bankTransfer);
        } else {
            // Microservice to get bank transfer
        }

        return null;
    }

    @Override
    public List<BankTransferResponse> findAll(String noAccount, Principal principal) {
        Account sourceAccount = validationService.validateCurrentUserHaveThisAccount(principal, noAccount);
        List<BankTransferResponse> bank = new ArrayList<>();
        bankTransferRepository.findAllByAccount(sourceAccount).forEach(bankTransfer ->
                bank.add(bankTransferMapper.toBankTransferResponse(bankTransfer))
        );
        return bank;
    }
}
