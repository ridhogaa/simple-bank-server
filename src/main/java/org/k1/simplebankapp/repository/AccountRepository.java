package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {

    List<Account> findAllByUser(User user);

    Optional<Account> findFirstByNo(String no);
}
