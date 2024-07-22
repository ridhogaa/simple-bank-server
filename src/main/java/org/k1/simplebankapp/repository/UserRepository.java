package org.k1.simplebankapp.repository;

import org.k1.simplebankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);

    Optional<User> findFirstByUsernameAndPin(String username, String pin);

    @Modifying
    @Query("UPDATE User u SET u.loginAttempts = ?1 WHERE u.username = ?2")
    void updateFailedAttempts(int loginAttempts, String username);
}
