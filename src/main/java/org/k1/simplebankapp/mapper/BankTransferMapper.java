package org.k1.simplebankapp.mapper;

import org.k1.simplebankapp.dto.BankTransferResponse;
import org.k1.simplebankapp.entity.BankTransfer;
import org.springframework.stereotype.Component;

@Component
public class BankTransferMapper {

    public BankTransferResponse toBankTransferResponse(
            BankTransfer bankTransfer
    ) {
        return BankTransferResponse
                .builder()
                .recipientName(bankTransfer.getRecipientName())
                .recipientAccountNo(bankTransfer.getRecipientAccountNo())
                .recipientBankName(bankTransfer.getBank().getBankName())
                .build();
    }
}
