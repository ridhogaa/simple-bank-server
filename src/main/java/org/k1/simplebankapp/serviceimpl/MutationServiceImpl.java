package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.dto.RequestNoAccount;
import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.Transaction;
import org.k1.simplebankapp.entity.User;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.k1.simplebankapp.mapper.MutationMapper;
import org.k1.simplebankapp.repository.AccountRepository;
import org.k1.simplebankapp.repository.TransactionRepository;
import org.k1.simplebankapp.repository.UserRepository;
import org.k1.simplebankapp.service.MutationService;
import org.k1.simplebankapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    private ValidationService validationService;

    @Override
    public List<MutationResponse> findAllByMonthAndMutationType(
            Integer month,
            MutationType type,
            RequestNoAccount noAccount,
            Pageable pageable,
            Principal principal
    ) {
        validationService.validate(noAccount);
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not valid, please login again!");
        }
        Account account = accountRepository.findFirstByNoAndUser(noAccount.getNoAccount(), user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "You dont have access to this account!"));
        Specification<Transaction> spec = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (month != null) {
                Expression<Integer> monthExpression = criteriaBuilder.function("MONTH", Integer.class, root.get("updatedDate"));
                predicates.add(criteriaBuilder.equal(monthExpression, month));
            }

            if (type != null) {
                if (type.equals(MutationType.PENGELUARAN)) {
                    predicates.add(criteriaBuilder.equal(root.get("account"), account));
                } else if (type.equals(MutationType.PEMASUKAN)) {
                    predicates.add(criteriaBuilder.equal(root.get("recipientTargetAccount"), account));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        List<MutationResponse> mutationResponseList = new ArrayList<>();
        transactionRepository.findAll(spec, pageable).forEach(transaction -> {
            mutationResponseList.add(mutationMapper.toMutationResponse(transaction, account.getNo()));
            log.info("DATA --> {}", mutationResponseList);
        });
        return mutationResponseList;
    }

    @Override
    public Map<String, Double> getSpendingAndIncome(Principal principal, MutationType type, String noAccount) {
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not valid, please login again!");
        }
        accountRepository.findFirstByNoAndUser(noAccount, user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
        HashMap<String, Double> map = new HashMap<>();
        if (type.equals(MutationType.PENGELUARAN)) {
            map.put("amount", transactionRepository.findSpending(noAccount));
        } else if (type.equals(MutationType.PEMASUKAN)) {
            map.put("amount", transactionRepository.findIncome(noAccount));
        }
        return map;
    }


}
