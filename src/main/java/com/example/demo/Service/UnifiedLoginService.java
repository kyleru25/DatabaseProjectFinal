package com.example.demo.Service;

import com.example.demo.Model.FlightProvider;
import com.example.demo.Model.LoginResult;
import com.example.demo.Model.Staff;
import com.example.demo.Model.User;
import com.example.demo.Repository.FlightProviderRepository;
import com.example.demo.Repository.StaffRepository;
import com.example.demo.Repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UnifiedLoginService {

    public static final String ADMIN_EMAIL = "admin@expedia.com";
    public static final String ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final FlightProviderRepository flightProviderRepository;

    public UnifiedLoginService(
            UserRepository userRepository,
            StaffRepository staffRepository,
            FlightProviderRepository flightProviderRepository
    ) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.flightProviderRepository = flightProviderRepository;
    }

    public LoginResult login(String emailAddress, String password) {
        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your email address.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your password.");
        }

        String cleanEmailAddress = emailAddress.trim().toLowerCase();

        if (ADMIN_EMAIL.equalsIgnoreCase(cleanEmailAddress) && ADMIN_PASSWORD.equals(password)) {
            return new LoginResult("ADMIN", "Expedia Administrator");
        }

        User user = userRepository.findByEmailAddressIgnoreCase(cleanEmailAddress).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            return new LoginResult("USER", user);
        }

        Staff staff = staffRepository.findByEmailAddressIgnoreCase(cleanEmailAddress).orElse(null);
        if (staff != null && staff.getPassword().equals(password)) {
            String staffStatus = staff.getStaffStatus();
            if (staffStatus == null || staffStatus.trim().isEmpty() || staffStatus.equalsIgnoreCase("Pending")) {
                throw new IllegalArgumentException("This staff account is still waiting for flight provider approval.");
            }
            if (staffStatus.equalsIgnoreCase("Rejected")) {
                throw new IllegalArgumentException("This staff account was rejected by the flight provider.");
            }
            if (!staffStatus.equalsIgnoreCase("Active")) {
                throw new IllegalArgumentException("This staff account is not active.");
            }
            return new LoginResult("STAFF", staff);
        }

        FlightProvider flightProvider = flightProviderRepository.findByEmailAddressIgnoreCase(cleanEmailAddress).orElse(null);
        if (flightProvider != null && flightProvider.getPassword().equals(password)) {
            String approvalStatus = flightProvider.getApprovalStatus();
            if (approvalStatus == null || approvalStatus.equalsIgnoreCase("Pending")) {
                throw new IllegalArgumentException("Your flight provider account is still waiting for admin approval.");
            }
            if (approvalStatus.equalsIgnoreCase("Rejected")) {
                throw new IllegalArgumentException("Your flight provider account was rejected by the admin.");
            }
            return new LoginResult("FLIGHT_PROVIDER", flightProvider);
        }

        throw new IllegalArgumentException("Invalid email address or password.");
    }
}
