package com.example.demo.Repository;

import com.example.demo.Model.Airplane;
import com.example.demo.Model.FlightProvider;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirplaneRepository extends JpaRepository<Airplane, Long> {

    List<Airplane> findByFlightProviderOrderByAirplaneNameAsc(FlightProvider flightProvider);

    List<Airplane> findByFlightProviderAndApprovalStatusIgnoreCaseOrderByAirplaneNameAsc(
            FlightProvider flightProvider,
            String approvalStatus
    );

    List<Airplane> findByApprovalStatusIgnoreCaseOrderByAirplaneNameAsc(String approvalStatus);

    Optional<Airplane> findByIdAndFlightProvider(Long id, FlightProvider flightProvider);

    boolean existsByAirplaneCodeIgnoreCaseAndFlightProvider(
            String airplaneCode,
            FlightProvider flightProvider
    );

    long countByApprovalStatusIgnoreCase(String approvalStatus);
}
