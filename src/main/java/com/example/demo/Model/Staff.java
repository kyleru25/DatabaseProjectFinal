package com.example.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String emailAddress;

    private String contactNumber;
    private String staffRole;
    private String password;

    /*
     * Staff approval flow:
     * Pending  - newly registered staff account waiting for the selected flight provider
     * Active   - approved by the selected flight provider and allowed to log in
     * Rejected - declined by the selected flight provider
     */
    private String staffStatus;

    @ManyToOne
    @JoinColumn(name = "flight_provider_id")
    private FlightProvider flightProvider;

    public Staff() {
    }

    public Staff(
            String firstName,
            String lastName,
            String emailAddress,
            String contactNumber,
            String staffRole,
            String password,
            String staffStatus,
            FlightProvider flightProvider
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.contactNumber = contactNumber;
        this.staffRole = staffRole;
        this.password = password;
        this.staffStatus = staffStatus;
        this.flightProvider = flightProvider;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(String staffStatus) {
        this.staffStatus = staffStatus;
    }

    public FlightProvider getFlightProvider() {
        return flightProvider;
    }

    public void setFlightProvider(FlightProvider flightProvider) {
        this.flightProvider = flightProvider;
    }
}
