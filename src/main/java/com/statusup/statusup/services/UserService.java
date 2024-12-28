package com.statusup.statusup.services;

import org.springframework.stereotype.Service;

import com.statusup.statusup.repositories.UserRepository;
import com.statusup.statusup.utils.JwtUtil;

@Service
public class UserService {

    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String getCurrentUsername() {
        return jwtUtil.getCurrentUserUsername();
    }

}
