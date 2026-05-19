package com.example.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FlightProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightProviderId;

    private String companyName;
    private String representativeFirstName;
    private String representativeLastName;
    private String emailAddress;
    private String contactNumber;
    private String businessPermitNumber;
    private String providerType;
    private String password;
    private String companyAddress;
    private String approvalStatus;

    public FlightProvider() {}

    public FlightProvider(
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
        this.companyName = companyName;
        this.representativeFirstName = representativeFirstName;
        this.representativeLastName = representativeLastName;
        this.emailAddress = emailAddress;
        this.contactNumber = contactNumber;
        this.businessPermitNumber = businessPermitNumber;
        this.providerType = providerType;
        this.password = password;
        this.companyAddress = companyAddress;
        this.approvalStatus = "Pending";
    }

    public Long getFlightProviderId() {
        return flightProviderId;
    }

    public void setFlightProviderId(Long flightProviderId) {
        this.flightProviderId = flightProviderId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRepresentativeFirstName() {
        return representativeFirstName;
    }

    public void setRepresentativeFirstName(String representativeFirstName) {
        this.representativeFirstName = representativeFirstName;
    }

    public String getRepresentativeLastName() {
        return representativeLastName;
    }

    public void setRepresentativeLastName(String representativeLastName) {
        this.representativeLastName = representativeLastName;
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

    public String getBusinessPermitNumber() {
        return businessPermitNumber;
    }

    public void setBusinessPermitNumber(String businessPermitNumber) {
        this.businessPermitNumber = businessPermitNumber;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
