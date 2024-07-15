package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.BankTransfer;
import org.k1.simplebankapp.entity.CashTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, Long>, JpaSpecificationExecutor<CashTransaction> {

}
