package com.statusup.statusup.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.statusup.statusup.exceptions.EmailTakenException;
import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.exceptions.UsernameTakenException;
import com.statusup.statusup.models.Role;
import com.statusup.statusup.models.User;
import com.statusup.statusup.models.UserRegistrationDTO;
import com.statusup.statusup.repositories.UserRepository;

@Service
public class RegistrationService {

    private UserRepository userRepository;
    private EmailService emailService;

    public RegistrationService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public boolean verifyToken(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find user with such verification token"));
        if (user.isEmailVerified()) {
            return false;
        }
        user.setEmailVerified(true);
        user.setVerificationToken(null); // Clear the token after verification
        userRepository.save(user);
        return true;
    }

    public ResponseEntity<?> register(UserRegistrationDTO registrationUser) {

        if (isUsernameAvailable(registrationUser.getUsername())) {
            throw new UsernameTakenException("Username is already taken");
        }
        if (isEmailAvailable(registrationUser.getEmail())) {
            throw new EmailTakenException("Email is already taken");
        }
        User user = new User();
        user.setUsername(registrationUser.getUsername());
        user.setEmail(registrationUser.getEmail());
        user.setEmailVerified(false);
        user.setVerificationToken(generateVerificationToken());
        user.setRole(Role.USER);
        // Encrypt the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(registrationUser.getPassword());
        user.setPassword(encryptedPassword);

        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());

        // Save the new user to repository
        userRepository.save(user);

        // Return log
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    private boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    private boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    private String generateVerificationToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }

}
