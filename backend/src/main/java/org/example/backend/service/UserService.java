package org.example.backend.service;

import org.example.backend.DTO.UserResponseDTO;
import org.example.backend.models.User;
import org.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    ///creating user
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        return userRepository.save(user);
    }

    ///finds user by a given email and verifies password
    public User findByEmail(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
                
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        
        if (!user.isActive()) {
            throw new AccessDeniedException("Account has been deactivated");
        }
        
        return user;
    }

    ///finds user by a given email without password verification (for internal use)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    ///returns list of users
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) && 
            !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))) {
            throw new AccessDeniedException("Admin access required");
        }
        return userRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllActiveUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) && 
            !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))) {
            throw new AccessDeniedException("Admin access required");
        }
        
        return userRepository.findByActiveTrue().stream()
                .map(user -> new UserResponseDTO(
                    user.getId(),
                    user.getEmail(),
                    user.isRole() ? "ADMIN" : "USER"
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deactivateUser(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) && 
            !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))) {
            throw new AccessDeniedException("Admin access required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isRole()) {
            throw new IllegalArgumentException("Cannot deactivate admin users");
        }

        user.setActive(false);
        userRepository.save(user);
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean isUserAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        System.out.println("Checking if user is admin - Email: " + email + ", Role: " + user.isRole());
        return user.isRole();
    }
} 