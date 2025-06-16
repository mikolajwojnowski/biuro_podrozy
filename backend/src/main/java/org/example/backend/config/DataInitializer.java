package org.example.backend.config;

import org.example.backend.models.User;
import org.example.backend.models.Trip;
import org.example.backend.repository.UserRepository;
import org.example.backend.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Klasa inicjalizująca dane początkowe w aplikacji.
 * Tworzy domyślne konta użytkowników (admin i user) oraz przykładowe wycieczki, jeśli nie istnieją.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /**
     * Repozytorium użytkowników.
     */
    private final UserRepository userRepository;

    /**
     * Repozytorium wycieczek.
     */
    private final TripRepository tripRepository;

    /**
     * Enkoder haseł.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Konstruktor wstrzykujący zależności.
     * @param userRepository repozytorium użytkowników
     * @param tripRepository repozytorium wycieczek
     * @param passwordEncoder enkoder haseł
     */
    @Autowired
    public DataInitializer(UserRepository userRepository, TripRepository tripRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Metoda uruchamiana po starcie aplikacji.
     * Tworzy domyślne konta użytkowników oraz przykładowe wycieczki, jeśli nie istnieją.
     * @param args argumenty wiersza poleceń
     */
    @Override
    public void run(String... args) {
        // Create admin account if it doesn't exist
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(true);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("Default admin account created");
        }

        // Create regular user account if it doesn't exist
        if (!userRepository.existsByEmail("user@example.com")) {
            User user = new User();
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user1234"));
            user.setRole(false);
            user.setActive(true);
            userRepository.save(user);
            System.out.println("Default user account created");
        }

        // Create sample trips if they don't exist
        if (tripRepository.count() == 0) {
            // First trip - Paris
            Trip parisTrip = new Trip();
            parisTrip.setTitle("Romantic Paris");
            parisTrip.setDescription("Experience the magic of the City of Light with our 5-day Paris tour. Visit the Eiffel Tower, Louvre Museum, and enjoy a Seine River cruise.");
            parisTrip.setTripDate(LocalDate.now().plusMonths(1));
            parisTrip.setDays(5);
            parisTrip.setPrice(1200.0);
            parisTrip.setCapacity(20);
            parisTrip.setAvailableSpots(20);
            parisTrip.setActive(true);
            tripRepository.save(parisTrip);
            System.out.println("Sample trip to Paris created");

            // Second trip - Rome
            Trip romeTrip = new Trip();
            romeTrip.setTitle("Historic Rome");
            romeTrip.setDescription("Discover the Eternal City with our 7-day Rome tour. Explore the Colosseum, Vatican City, and enjoy authentic Italian cuisine.");
            romeTrip.setTripDate(LocalDate.now().plusMonths(2));
            romeTrip.setDays(7);
            romeTrip.setPrice(1500.0);
            romeTrip.setCapacity(15);
            romeTrip.setAvailableSpots(15);
            romeTrip.setActive(true);
            tripRepository.save(romeTrip);
            System.out.println("Sample trip to Rome created");
        }
    }
} 