package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.Ewallet;
import org.k1.simplebankapp.entity.Mutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MutationRepository extends JpaRepository<Mutation, Long>, JpaSpecificationExecutor<Mutation> {

}
