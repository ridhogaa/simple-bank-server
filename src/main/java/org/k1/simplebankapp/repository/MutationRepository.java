package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.Ewallet;
import org.k1.simplebankapp.entity.Mutation;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface MutationRepository extends JpaRepository<Mutation, Long>, JpaSpecificationExecutor<Mutation> {

//    @Query(
//            value = "SELECT m FROM Mutation m JOIN m.transaction t JOIN t.account a WHERE FUNCTION('MONTH', m.timestamp) = :month AND FUNCTION('DAY', m.timestamp) = :day AND m.mutationType = :type AND a.no = :noAccount"
//            , nativeQuery = true)
//    Page<Mutation> findMutation(
//            @Param("month") int month,
//            @Param("day") int day,
//            @Param("type") MutationType type,
//            @Param("noAccount") String noAccount,
//            Pageable pageable
//    );

}
