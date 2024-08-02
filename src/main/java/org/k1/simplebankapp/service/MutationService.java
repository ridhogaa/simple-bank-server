package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.dto.RequestNoAccount;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface MutationService {
    Page<MutationResponse> findAllByMonthAndMutationType(
            Integer month,
            MutationType type,
            String noAccount,
            Pageable pageable,
            Principal principal
    );

    Map<String, Double> getSpendingAndIncome(
            Principal principal,
            String noAccount
    );
}
