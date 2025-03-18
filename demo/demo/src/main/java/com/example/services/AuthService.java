package com.example.services;

// package com.jobportal.services;

import com.example.models.User;
import com.example.models.Role;

import com.example.repositories.UserRepository;
import com.example.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(String email, String password, String role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role.equalsIgnoreCase("EMPLOYER") ? Role.EMPLOYER : Role.JOB_SEEKER);
        userRepository.save(user);
        return jwtUtil.generateToken(email);
    }

    public String authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return jwtUtil.generateToken(email);
        }
        throw new RuntimeException("Invalid credentials.");
    }
}
