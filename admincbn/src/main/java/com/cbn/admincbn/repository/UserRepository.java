package com.cbn.admincbn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cbn.admincbn.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}