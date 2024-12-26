package com.statusup.statusup.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.AuthRequest;
import com.statusup.statusup.models.User;
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
    public String register(@RequestBody User user) {
        return registrationService.register(user);
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        return jwtUtil.generateToken(authRequest.getUsername());
    }

    
}
