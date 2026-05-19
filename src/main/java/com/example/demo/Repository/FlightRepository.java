package com.example.demo.Repository;

import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightProvider;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByFlightProviderOrderByFlightDateAscFlightTimeAsc(FlightProvider flightProvider);

    boolean existsByFlightNumberIgnoreCaseAndFlightProvider(
            String flightNumber,
            FlightProvider flightProvider
    );

    Optional<Flight> findByIdAndFlightProvider(Long id, FlightProvider flightProvider);

    List<Flight> findByApprovalStatusIgnoreCaseOrderByFlightDateAscFlightTimeAsc(String approvalStatus);

    List<Flight> findByFlightStatusIgnoreCaseAndApprovalStatusIgnoreCaseAndFlightProvider_ApprovalStatusIgnoreCaseAndAirplane_ApprovalStatusIgnoreCaseOrderByFlightDateAscFlightTimeAsc(
            String flightStatus,
            String approvalStatus,
            String providerApprovalStatus,
            String airplaneApprovalStatus
    );

    List<Flight> findByOriginContainingIgnoreCaseAndDestinationContainingIgnoreCaseAndFlightDateAndFlightStatusIgnoreCaseAndApprovalStatusIgnoreCaseAndFlightProvider_ApprovalStatusIgnoreCaseAndAirplane_ApprovalStatusIgnoreCaseOrderByFlightTimeAsc(
            String origin,
            String destination,
            LocalDate flightDate,
            String flightStatus,
            String approvalStatus,
            String providerApprovalStatus,
            String airplaneApprovalStatus
    );

    Optional<Flight> findByIdAndFlightStatusIgnoreCaseAndApprovalStatusIgnoreCaseAndFlightProvider_ApprovalStatusIgnoreCaseAndAirplane_ApprovalStatusIgnoreCase(
            Long id,
            String flightStatus,
            String approvalStatus,
            String providerApprovalStatus,
            String airplaneApprovalStatus
    );

    List<Flight> findByDestinationContainingIgnoreCaseAndFlightStatusIgnoreCaseAndApprovalStatusIgnoreCaseAndFlightProvider_ApprovalStatusIgnoreCaseAndAirplane_ApprovalStatusIgnoreCaseOrderByFlightDateAscFlightTimeAsc(
            String destination,
            String flightStatus,
            String approvalStatus,
            String providerApprovalStatus,
            String airplaneApprovalStatus
    );

    long countByApprovalStatusIgnoreCase(String approvalStatus);
}
