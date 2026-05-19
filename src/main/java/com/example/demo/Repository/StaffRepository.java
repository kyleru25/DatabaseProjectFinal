package com.example.demo.Repository;

import com.example.demo.Model.FlightProvider;
import com.example.demo.Model.Staff;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    boolean existsByEmailAddressIgnoreCase(String emailAddress);

    Optional<Staff> findByEmailAddressIgnoreCase(String emailAddress);

    List<Staff> findByFlightProviderOrderByFirstNameAscLastNameAsc(FlightProvider flightProvider);

    List<Staff> findByFlightProviderAndStaffStatusIgnoreCaseOrderByFirstNameAscLastNameAsc(
            FlightProvider flightProvider,
            String staffStatus
    );

    long countByFlightProviderAndStaffStatusIgnoreCase(FlightProvider flightProvider, String staffStatus);
}
