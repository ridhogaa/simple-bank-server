package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.User;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.k1.simplebankapp.mapper.MutationMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.TransactionRepository;
import org.k1.simplebankapp.repository.UserRepository;
import org.k1.simplebankapp.service.MutationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MutationServiceImpl implements MutationService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MutationMapper mutationMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<MutationResponse> findAllByMonthAndDayAndType(
            int month,
            int day,
            MutationType type,
            String noAccount,
            Pageable pageable,
            Principal principal
    ) {
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not valid, please login again!");
        }
        Account account = accountRepository.findFirstByNoAndUser(noAccount, user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
        List<MutationResponse> mutationResponseList = new ArrayList<>();
        if (type == MutationType.PENGELUARAN) {
            transactionRepository.findAllAsThisAccount(account.getNo(), month, day, pageable).forEach(transaction -> {
                mutationResponseList.add(mutationMapper.toMutationResponse(transaction));
            });
        } else if (type == MutationType.PEMASUKAN) {
            transactionRepository.findAllAsRecipientAccount(account.getNo(), month, day, pageable).forEach(transaction -> {
                mutationResponseList.add(mutationMapper.toMutationResponse(transaction));
            });
        }
        return mutationResponseList;
    }
}
