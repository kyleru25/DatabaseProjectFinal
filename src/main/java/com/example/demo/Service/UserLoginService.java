package com.example.demo.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserLoginService {

    private final UserRepository userRepository;

    public UserLoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loginUser(String emailAddress, String password) {
        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your email address.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your password.");
        }

        User user = userRepository
                .findByEmailAddressIgnoreCase(emailAddress.trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email address or password."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email address or password.");
        }

        return user;
    }
}