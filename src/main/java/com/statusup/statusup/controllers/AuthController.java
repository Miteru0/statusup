package com.statusup.statusup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.AuthRequest;
import com.statusup.statusup.models.RegisterRequest;
import com.statusup.statusup.services.RegistrationService;
import com.statusup.statusup.utils.JwtUtil;

@RestController
public class AuthController {

    private RegistrationService registrationService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public AuthController(RegistrationService registrationService, AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest user) {
        return registrationService.register(user);
    }

    @GetMapping("register/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        boolean isVerified = registrationService.verifyToken(token);
        if (isVerified) {
            return ResponseEntity.ok("Email verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        return jwtUtil.generateToken(authRequest.getUsername());
    }

    
}
