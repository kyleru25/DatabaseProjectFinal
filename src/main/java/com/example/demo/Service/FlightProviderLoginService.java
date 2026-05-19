package com.example.demo.Service;

import com.example.demo.Model.FlightProvider;
import com.example.demo.Repository.FlightProviderRepository;

import org.springframework.stereotype.Service;

@Service
public class FlightProviderLoginService {

    private final FlightProviderRepository flightProviderRepository;

    public FlightProviderLoginService(FlightProviderRepository flightProviderRepository) {
        this.flightProviderRepository = flightProviderRepository;
    }

    public FlightProvider loginFlightProvider(String emailAddress, String password) {

        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your email address.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Please enter your password.");
        }

        String cleanEmailAddress = emailAddress.trim();

        FlightProvider flightProvider = flightProviderRepository
                .findByEmailAddressIgnoreCase(cleanEmailAddress)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email address or password."));

        if (!flightProvider.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email address or password.");
        }

        return flightProvider;
    }

}
