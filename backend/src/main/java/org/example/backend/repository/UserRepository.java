package org.example.backend.repository;

import org.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   //Spring Boot automatically provides an implementation for the repository, giving access to common methods like save(), findById(), findAll(), and deleteById().

   Optional<User> findByEmail(String email);
   List<User> findByActiveTrue();
   boolean existsByEmail(String email);
   long countByRoleTrue();
}
