package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.TopupEwallet;
import org.k1.simplebankapp.entity.Transaction;
import org.k1.simplebankapp.entity.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findFirstByIdAndTransactionType(String id, TransactionType transactionType);


}
