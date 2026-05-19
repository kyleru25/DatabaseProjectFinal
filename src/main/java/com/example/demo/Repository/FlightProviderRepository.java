package com.example.demo.Repository;

import com.example.demo.Model.FlightProvider;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlightProviderRepository extends JpaRepository<FlightProvider, Long> {
    
    boolean existsByFlightProviderId(Long flightProviderId);

    boolean existsByCompanyName(String companyName);

    boolean existsByEmailAddress(String emailAddress);

    boolean existsByCompanyNameIgnoreCase(String companyName);

    boolean existsByEmailAddressIgnoreCase(String emailAddress);

    Optional<FlightProvider> findByEmailAddressIgnoreCase(String emailAddress);

    List<FlightProvider> findByApprovalStatusIgnoreCaseOrderByCompanyNameAsc(String approvalStatus);

    long countByApprovalStatusIgnoreCase(String approvalStatus);
}
