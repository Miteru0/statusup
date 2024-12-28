package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.ExceptionDTO;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HelloController {

    @GetMapping("/test")
    public String test() {
        return "Test complete!";
    }

    @GetMapping("/test/exception")
    public Object testException() {
        return new ExceptionDTO("Testing Exception DTO", "Test complete!");
    }
    
    
}
