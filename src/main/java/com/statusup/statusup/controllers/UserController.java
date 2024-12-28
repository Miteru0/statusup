package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getCurrentUsername() {
        return userService.getCurrentUsername();
    }
    

}
