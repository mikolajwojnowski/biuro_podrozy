package org.example.backend.repository;

import org.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   //Spring Boot automatically provides an implementation for the repository, giving access to common methods like save(), findById(), findAll(), and deleteById().

   Optional<User> findByEmail(String email);
}
