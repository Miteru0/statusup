package com.statusup.statusup.services;

import org.springframework.stereotype.Service;

import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.models.User;
import com.statusup.statusup.models.UserDTO;
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

    public UserDTO getUserInformation(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find user with such username"));
        return new UserDTO(user.getUsername(), user.getEmail());
    }

    public UserDTO getCurrentUserInformation() {
        return getUserInformation(getCurrentUsername());
    }

}
