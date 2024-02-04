package com.sumerge.authservice.repository;

import com.sumerge.authservice.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity , Integer > {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);

}
