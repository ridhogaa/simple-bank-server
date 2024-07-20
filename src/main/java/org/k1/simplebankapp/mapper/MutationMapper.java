package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.MutationResponse;
import org.k1.simplebankapp.entity.Mutation;
import org.springframework.stereotype.Component;

@Component
public class MutationMapper {

    public MutationResponse toMutationResponse(Mutation mutation) {
        return MutationResponse.builder()
                .transactionType(mutation.getTransaction().getTransactionType().name())
                .amount(mutation.getTransaction().getAmount())
                .date(mutation.getTransaction().getTransactionDate().toString())
                .recipientTargetAccount(mutation.getTransaction().getRecipientTargetAccount().getNo())
                .build();
    }
}
