package com.example.demo.Service;

import com.example.demo.Model.FlightProvider;
import com.example.demo.Repository.FlightProviderRepository;

import org.springframework.stereotype.Service;

@Service
public class FlightProviderRegistrationService {

    private final FlightProviderRepository flightProviderRepository;

    public FlightProviderRegistrationService(FlightProviderRepository flightProviderRepository) {
        this.flightProviderRepository = flightProviderRepository;
    }

    public void registerFlightProvider(
            String companyName,
            String representativeFirstName,
            String representativeLastName,
            String emailAddress,
            String contactNumber,
            String businessPermitNumber,
            String providerType,
            String password,
            String companyAddress
    ) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name is required.");
        }

        if (representativeFirstName == null || representativeFirstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Representative first name is required.");
        }

        if (representativeLastName == null || representativeLastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Representative last name is required.");
        }

        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is required.");
        }

        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact number is required.");
        }

        if (businessPermitNumber == null || businessPermitNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Business permit number is required.");
        }

        if (providerType == null || providerType.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider type is required.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (companyAddress == null || companyAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Company address is required.");
        }

        String cleanCompanyName = companyName.trim();
        String cleanEmailAddress = emailAddress.trim().toLowerCase();

        boolean companyNameExists = flightProviderRepository.existsByCompanyNameIgnoreCase(cleanCompanyName);
        boolean emailAddressExists = flightProviderRepository.existsByEmailAddressIgnoreCase(cleanEmailAddress);

        if (companyNameExists) {
            throw new IllegalArgumentException("Company name already exists.");
        }

        if (emailAddressExists) {
            throw new IllegalArgumentException("Email address already exists.");
        }

        FlightProvider newFlightProvider = new FlightProvider(
                cleanCompanyName,
                representativeFirstName.trim(),
                representativeLastName.trim(),
                cleanEmailAddress,
                contactNumber.trim(),
                businessPermitNumber.trim(),
                providerType.trim(),
                password,
                companyAddress.trim()
        );
        newFlightProvider.setApprovalStatus("Pending");
        flightProviderRepository.save(newFlightProvider);
    }
}
