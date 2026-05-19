package com.example.demo.Service;

import com.example.demo.Model.Staff;
import com.example.demo.Repository.StaffRepository;

import org.springframework.stereotype.Service;

@Service
public class StaffLoginService {

    private final StaffRepository staffRepository;

    public StaffLoginService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff loginStaff(String emailAddress, String password, String staffRole) {

        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your staff email address.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your password.");
        }

        if (staffRole == null || staffRole.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select your staff role.");
        }

        String cleanEmailAddress = emailAddress.trim().toLowerCase();

        Staff staff = staffRepository
                .findByEmailAddressIgnoreCase(cleanEmailAddress)
                .orElseThrow(() -> new IllegalArgumentException("Invalid staff email or password."));

        if (!staff.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid staff email or password.");
        }

        if (!staff.getStaffRole().equalsIgnoreCase(staffRole.trim())) {
            throw new IllegalArgumentException("Selected staff role does not match this account.");
        }

        if (staff.getStaffStatus() == null || staff.getStaffStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("This staff account is waiting for flight provider approval.");
        }

        if (staff.getStaffStatus().equalsIgnoreCase("Pending")) {
            throw new IllegalArgumentException("This staff account is still waiting for flight provider approval.");
        }

        if (staff.getStaffStatus().equalsIgnoreCase("Rejected")) {
            throw new IllegalArgumentException("This staff account was rejected by the flight provider.");
        }

        if (!staff.getStaffStatus().equalsIgnoreCase("Active")) {
            throw new IllegalArgumentException("This staff account is not active.");
        }

        return staff;
    }
}