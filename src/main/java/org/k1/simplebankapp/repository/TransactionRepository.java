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

    @Query(value = "SELECT SUM(t.amount) " +
            "FROM transactions t " +
            "WHERE t.status = 'SUCCESS'" +
            "AND t.account_id = :accountId " +
            "AND EXTRACT(MONTH FROM created_date) = :month", nativeQuery = true)
    Optional<Double> findSpending(@Param("accountId") String accountId, @Param("month") Integer month);

    @Query(value = "SELECT SUM(t.amount) " +
            "FROM transactions t " +
            "WHERE t.status = 'SUCCESS'" +
            "AND t.recipient_target_account = :accountId " +
            "AND EXTRACT(MONTH FROM created_date) = :month", nativeQuery = true)
    Optional<Double> findIncome(@Param("accountId") String accountId, @Param("month") Integer month);
}
