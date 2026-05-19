package com.example.demo.Service;

import com.example.demo.Model.FlightProvider;
import com.example.demo.Model.Staff;
import com.example.demo.Repository.FlightProviderRepository;
import com.example.demo.Repository.StaffRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffRegistrationService {

    private final StaffRepository staffRepository;
    private final FlightProviderRepository flightProviderRepository;

    public StaffRegistrationService(
            StaffRepository staffRepository,
            FlightProviderRepository flightProviderRepository
    ) {
        this.staffRepository = staffRepository;
        this.flightProviderRepository = flightProviderRepository;
    }

    public List<FlightProvider> getApprovedFlightProviders() {
        return flightProviderRepository.findByApprovalStatusIgnoreCaseOrderByCompanyNameAsc("Approved");
    }

    public void registerStaff(
            String firstName,
            String lastName,
            String emailAddress,
            String contactNumber,
            String staffRole,
            String password,
            Long flightProviderId
    ) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required.");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required.");
        }

        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is required.");
        }

        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact number is required.");
        }

        if (staffRole == null || staffRole.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff role is required.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (flightProviderId == null) {
            throw new IllegalArgumentException("Please select the flight provider company this staff account belongs to.");
        }

        FlightProvider flightProvider = flightProviderRepository.findById(flightProviderId)
                .orElseThrow(() -> new IllegalArgumentException("Selected flight provider was not found."));

        if (!"Approved".equalsIgnoreCase(flightProvider.getApprovalStatus())) {
            throw new IllegalArgumentException("Selected flight provider is not approved by the admin yet.");
        }

        String cleanEmailAddress = emailAddress.trim().toLowerCase();

        boolean emailExists = staffRepository.existsByEmailAddressIgnoreCase(cleanEmailAddress);

        if (emailExists) {
            throw new IllegalArgumentException("Staff email address already exists.");
        }

        Staff staff = new Staff(
                firstName.trim(),
                lastName.trim(),
                cleanEmailAddress,
                contactNumber.trim(),
                staffRole.trim(),
                password,
                "Pending",
                flightProvider
        );

        staffRepository.save(staff);
    }
}
