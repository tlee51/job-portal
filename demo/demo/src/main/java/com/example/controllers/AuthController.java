package com.example.controllers;

// package com.jobportal.controllers;

import com.example.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String token = authService.registerUser(request.get("email"), request.get("password"), request.get("role"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String token = authService.authenticateUser(request.get("email"), request.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }
}

