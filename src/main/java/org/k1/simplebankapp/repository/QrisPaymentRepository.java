package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.QrisPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrisPaymentRepository extends JpaRepository<QrisPayment, Long>, JpaSpecificationExecutor<QrisPayment> {
    Optional<QrisPayment> findByQrisCode(String qrisCode);
}
