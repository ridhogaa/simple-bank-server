package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.Bank;
import org.k1.simplebankapp.entity.BankTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransferRepository extends JpaRepository<BankTransfer, Long>, JpaSpecificationExecutor<BankTransfer> {

}
