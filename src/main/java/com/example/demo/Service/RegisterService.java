package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

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

        firstName = firstName.trim();

        if (!firstName.matches("^[A-Za-z\\s'-]{2,50}$")) {
            throw new IllegalArgumentException(
                    "First name must only contain letters!"
            );
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required!");
        }

        lastName = lastName.trim();

        if (!lastName.matches("^[A-Za-z\\s'-]{2,50}$")) {
            throw new IllegalArgumentException(
                    "Last name must only contain letters!"
            );
        }

        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Email address is required!"
            );
        }

        String cleanEmailAddress =
                emailAddress.trim().toLowerCase();

        if (!cleanEmailAddress.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            throw new IllegalArgumentException(
                    "Invalid email address!"
            );
        }

        boolean emailAddressExists =
                userRepo.existsByEmailAddressIgnoreCase(
                        cleanEmailAddress
                );

        if (emailAddressExists) {
            throw new IllegalArgumentException(
                    "Email address already taken!"
            );
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Phone number is required!"
            );
        }

        phoneNumber = phoneNumber.trim();

        if (!phoneNumber.matches("^09\\d{9}$")) {
            throw new IllegalArgumentException(
                    "Phone number must start with 09 and contain 11 digits!"
            );
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Password is required!"
            );
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters!"
            );
        }

        if (!password.matches(
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$"
        )) {
            throw new IllegalArgumentException(
                    "Password must contain uppercase, lowercase, and a number!"
            );
        }

        User newUser = new User(
                firstName,
                lastName,
                cleanEmailAddress,
                phoneNumber,
                password
        );

        userRepo.save(newUser);
    }
}