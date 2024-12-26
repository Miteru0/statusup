package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {

    @GetMapping("/test")
    public String getMethodName() {
        return "Test complete!";
    }
    
}
