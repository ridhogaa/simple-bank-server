package org.k1.simplebankapp.repository;


import org.k1.simplebankapp.entity.oauth2.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Client findOneByClientId(String clientId);

}

