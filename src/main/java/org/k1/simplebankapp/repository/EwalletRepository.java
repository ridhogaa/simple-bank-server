package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.Bank;
import org.k1.simplebankapp.entity.Ewallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EwalletRepository extends JpaRepository<Ewallet, Long>, JpaSpecificationExecutor<Ewallet> {

}
