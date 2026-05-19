package com.example.demo.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository userRepo;

    public RegisterService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void userRegistration(
            String firstName,
            String lastName,
            String emailAddress,
            String phoneNumber,
            String password
    ) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required!");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required!");
        }

        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is required!");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required!");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required!");
        }

        String cleanEmailAddress = emailAddress.trim().toLowerCase();

        boolean emailAddressExists = userRepo.existsByEmailAddressIgnoreCase(cleanEmailAddress);

        if (emailAddressExists) {
            throw new IllegalArgumentException("Email address already taken!");
        }

        User newUser = new User(
                firstName.trim(),
                lastName.trim(),
                cleanEmailAddress,
                phoneNumber.trim(),
                password
        );

        userRepo.save(newUser);
    }
}