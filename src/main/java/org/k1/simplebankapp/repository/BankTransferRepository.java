package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.BankTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankTransferRepository extends JpaRepository<BankTransfer, Long>, JpaSpecificationExecutor<BankTransfer> {
    Optional<BankTransfer> findFirstByAccountAndRecipientAccountNo(Account account, String recipientAccountNo);

    List<BankTransfer> findAllByAccount(Account account);
}
