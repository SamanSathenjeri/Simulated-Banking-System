package com.example.transaction_validator.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User loginRequest = new User();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        String token = userService.authenticate(loginRequest);
        return ResponseEntity.ok(Map.of("jwt", token));
    }
}

