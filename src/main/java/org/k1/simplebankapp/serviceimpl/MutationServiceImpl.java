package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.User;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.k1.simplebankapp.mapper.MutationMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.MutationRepository;
import org.k1.simplebankapp.repository.UserRepository;
import org.k1.simplebankapp.service.MutationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MutationServiceImpl implements MutationService {

    @Autowired
    private MutationRepository mutationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MutationMapper mutationMapper;

    @Override
    public List<MutationResponse> findAllByMonthAndDayAndType(
            int month,
            int day,
            MutationType type,
            String noAccount,
            Pageable pageable
    ) {
        Account account = accountRepository.findFirstByNo(noAccount).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
        List<MutationResponse> mutationResponseList = new ArrayList<>();
//        mutationRepository.findMutation(month, day, type, account.getNo(), pageable).forEach(
//                mutation -> mutationResponseList.add(mutationMapper.toMutationResponse(mutation))
//        );
        return mutationResponseList;
    }
}
