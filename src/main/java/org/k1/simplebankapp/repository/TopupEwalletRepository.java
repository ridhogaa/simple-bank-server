package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.QrisPayment;
import org.k1.simplebankapp.entity.TopupEwallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TopupEwalletRepository extends JpaRepository<TopupEwallet, Long>, JpaSpecificationExecutor<TopupEwallet> {

}
