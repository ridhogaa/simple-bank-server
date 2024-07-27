package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {

    List<Account> findAllByUser(User user);

    Optional<Account> findFirstByNo(String no);

    Optional<Account> findFirstByNoAndUser(String no, User user);

    @Modifying
    @Query("update Account set balance = balance + :amount where no = :no")
    void deposit(@Param("no") String no, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("update Account set balance = balance - :amount where no = :no")
    void withdraw(@Param("no") String no, @Param("amount") BigDecimal amount);


    @Modifying
    @Query("update Account set pinAttempts = pinAttempts + :attempts where no = :no")
    void setAttempts(@Param("attempts") Integer attempts, @Param("no") String no);
}
