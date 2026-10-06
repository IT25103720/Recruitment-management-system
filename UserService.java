package com.lankajobshub.service;

import com.lankajobshub.model.User;
import com.lankajobshub.model.UserRole;
import com.lankajobshub.model.UserStatus;
import com.lankajobshub.exception.UserNotFoundException;
import com.lankajobshub.repository.UserRepository;
import com.lankajobshub.config.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;



    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedDate(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedUser);
        } catch (Exception e) {
            // Log error but don't fail registration
            System.err.println("Error sending welcome email: " + e.getMessage());
        }
        
        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public User updateUser(User user) {
        User existing = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %s", user.getId())));
        
        // Core fields allowed to update
        existing.setEmail(user.getEmail());
        existing.setRole(user.getRole());
        existing.setStatus(user.getStatus());

        // Profile fields
        existing.setFullName(user.getFullName());
        existing.setPhoneNumber(user.getPhoneNumber());
        existing.setBio(user.getBio());
        existing.setLinkedinUrl(user.getLinkedinUrl());
        existing.setGithubUrl(user.getGithubUrl());
        
        return userRepository.save(existing);
    }

    public User updateResumePath(Long userId, String resumePath) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %s", userId)));

        existing.setResumePath(resumePath);
        return userRepository.save(existing);
    }

    public User updateProfilePhotoPath(Long userId, String profilePhotoPath) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %s", userId)));

        existing.setProfilePhotoPath(profilePhotoPath);
        return userRepository.save(existing);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        
        return user.getStatus() == UserStatus.ACTIVE && 
               passwordEncoder.matches(password, user.getPassword());
    }

    public void updateLastLogin(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    public User changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %s", userId)));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        System.out.println("Attempting to load user with email: " + email);
        System.out.println("User found. Encoded password from DB: " + user.getPassword());

        return new CustomUserDetails(user);
    }
}
