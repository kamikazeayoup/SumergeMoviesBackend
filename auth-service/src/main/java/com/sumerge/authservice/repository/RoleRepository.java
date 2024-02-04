package com.sumerge.authservice.repository;

import com.sumerge.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role , Integer> {
    Optional<Role> findByName(String name);
}
