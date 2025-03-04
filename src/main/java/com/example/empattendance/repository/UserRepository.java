package com.example.empattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.empattendance.model.User;

import java.util.Optional;

/** This interface represents the repository for users. */
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
