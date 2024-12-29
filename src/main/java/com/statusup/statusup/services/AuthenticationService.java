package com.statusup.statusup.services;

import org.springframework.stereotype.Service;

import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.models.User;
import com.statusup.statusup.repositories.UserRepository;
import com.statusup.statusup.utils.JwtUtil;

@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                                   .orElseThrow(() -> new ResourceNotFoundException("User with username" + username + " not found"));

        // Compare password (use a password encoder for hashed passwords)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT Token
        return jwtUtil.generateToken(username);
    }

}
