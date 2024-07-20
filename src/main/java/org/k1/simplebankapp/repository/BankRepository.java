package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.Account;
import org.k1.simplebankapp.entity.Bank;
import org.k1.simplebankapp.entity.BankTransfer;
import org.k1.simplebankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long>, JpaSpecificationExecutor<Bank> {

}
