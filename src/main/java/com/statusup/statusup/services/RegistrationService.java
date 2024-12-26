package com.statusup.statusup.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.statusup.statusup.models.User;
import com.statusup.statusup.repositories.UserRepository;

@Service
public class RegistrationService {

    private UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(User user) {

        // Encrypt the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        
        // Save the new user to repository
        userRepository.save(user);

        // Return log
        return "User registered successfully";
    }

}
