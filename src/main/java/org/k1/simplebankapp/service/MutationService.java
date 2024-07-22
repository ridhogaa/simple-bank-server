package org.k1.simplebankapp.service;

import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.dto.RequestNoAccount;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface MutationService {
    List<MutationResponse> findAllByMonthAndMutationType(
            Integer month,
            MutationType type,
            RequestNoAccount noAccount,
            Pageable pageable,
            Principal principal
    );
}
