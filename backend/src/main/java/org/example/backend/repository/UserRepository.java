package org.example.backend.repository;

import org.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Interfejs repozytorium dla encji User.
 * Zapewnia metody do zarządzania użytkownikami w bazie danych.
 * Spring Boot automatycznie dostarcza implementację repozytorium, dając dostęp do typowych metod
 * jak save(), findById(), findAll() i deleteById().
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   //Spring Boot automatically provides an implementation for the repository, giving access to common methods like save(), findById(), findAll(), and deleteById().

   /**
    * Znajduje użytkownika po adresie email.
    * @param email adres email użytkownika
    * @return opcjonalny użytkownik, jeśli istnieje
    */
   Optional<User> findByEmail(String email);

   /**
    * Znajduje wszystkie aktywne konta użytkowników.
    * @return lista wszystkich aktywnych użytkowników
    */
   List<User> findByActiveTrue();

   /**
    * Sprawdza czy istnieje użytkownik o podanym adresie email.
    * @param email adres email do sprawdzenia
    * @return true jeśli użytkownik istnieje, false w przeciwnym razie
    */
   boolean existsByEmail(String email);

   /**
    * Zlicza liczbę użytkowników z uprawnieniami administratora.
    * @return liczba użytkowników z uprawnieniami administratora
    */
   long countByRoleTrue();
}
