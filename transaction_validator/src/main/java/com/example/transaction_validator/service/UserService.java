package com.example.transaction_validator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction_validator.dto.UserDTO;
import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.repository.UserRepository;
import com.example.transaction_validator.security.JwtUtil;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UserDTO createUser(User user){
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("User with this email already exists.");
        }
        String hashed_password = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed_password);
        return mapToDTO(userRepository.save(user));
    }

    public User getUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).get();
    }

    public UserDTO getUserByEmail(String email){
        return mapToDTO(userRepository.findByEmail(email).get());
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Transactional
    public UserDTO updateUser(User user) {
        return mapToDTO(userRepository.save(user));
    }

    @Transactional
    public boolean deactivateUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        if (user.getAccounts() != null) {
            user.getAccounts().forEach(a -> a.setActive(false));
        }

        userRepository.save(user);
        return true;
    }

    public String authenticate(User user){
        User storedUser = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new IllegalStateException("There is no User with this email."));

        if (!passwordEncoder.matches(user.getPassword(), storedUser.getPassword())){
            throw new IllegalStateException("Incorrect Password");
        }

        if (!storedUser.isActive()){
            throw new RuntimeException("User does not exist");
        }
        return jwtUtil.generateToken(storedUser.getEmail());
    }

    public UserDTO mapToDTO(User user){
        return new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getUserRole());
    }
}
