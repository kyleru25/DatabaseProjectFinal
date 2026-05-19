package com.example.demo.Service;

import com.example.demo.Model.Airplane;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightProvider;
import com.example.demo.Repository.AirplaneRepository;
import com.example.demo.Repository.FlightProviderRepository;
import com.example.demo.Repository.FlightRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminDashboardService {

    private final FlightProviderRepository flightProviderRepository;
    private final AirplaneRepository airplaneRepository;
    private final FlightRepository flightRepository;

    public AdminDashboardService(
            FlightProviderRepository flightProviderRepository,
            AirplaneRepository airplaneRepository,
            FlightRepository flightRepository
    ) {
        this.flightProviderRepository = flightProviderRepository;
        this.airplaneRepository = airplaneRepository;
        this.flightRepository = flightRepository;
    }

    public List<FlightProvider> getPendingProviders() {
        return flightProviderRepository.findAll()
                .stream()
                .filter(provider -> isPending(provider.getApprovalStatus()))
                .sorted((a, b) -> safe(a.getCompanyName()).compareToIgnoreCase(safe(b.getCompanyName())))
                .toList();
    }

    public List<Airplane> getPendingAirplanes() {
        return airplaneRepository.findAll()
                .stream()
                .filter(airplane -> isPending(airplane.getApprovalStatus()))
                .sorted((a, b) -> safe(a.getAirplaneName()).compareToIgnoreCase(safe(b.getAirplaneName())))
                .toList();
    }

    public List<Flight> getPendingFlights() {
        return flightRepository.findAll()
                .stream()
                .filter(flight -> isPending(flight.getApprovalStatus()))
                .sorted((a, b) -> safe(a.getFlightNumber()).compareToIgnoreCase(safe(b.getFlightNumber())))
                .toList();
    }

    @Transactional
    public void approveProvider(Long providerId) {
        FlightProvider provider = flightProviderRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Flight provider not found."));
        provider.setApprovalStatus("Approved");
        flightProviderRepository.save(provider);
    }

    @Transactional
    public void rejectProvider(Long providerId) {
        FlightProvider provider = flightProviderRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Flight provider not found."));
        provider.setApprovalStatus("Rejected");
        flightProviderRepository.save(provider);
    }

    @Transactional
    public void approveAirplane(Long airplaneId) {
        Airplane airplane = airplaneRepository.findById(airplaneId)
                .orElseThrow(() -> new IllegalArgumentException("Airplane not found."));

        if (airplane.getFlightProvider() == null
                || !"Approved".equalsIgnoreCase(airplane.getFlightProvider().getApprovalStatus())) {
            throw new IllegalArgumentException("Approve the flight provider first before approving this airplane.");
        }

        airplane.setApprovalStatus("Approved");
        airplaneRepository.save(airplane);
    }

    @Transactional
    public void rejectAirplane(Long airplaneId) {
        Airplane airplane = airplaneRepository.findById(airplaneId)
                .orElseThrow(() -> new IllegalArgumentException("Airplane not found."));
        airplane.setApprovalStatus("Rejected");
        airplaneRepository.save(airplane);
    }

    @Transactional
    public void approveFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found."));

        if (flight.getFlightProvider() == null
                || !"Approved".equalsIgnoreCase(flight.getFlightProvider().getApprovalStatus())) {
            throw new IllegalArgumentException("Approve the flight provider first before approving this flight.");
        }

        if (flight.getAirplane() == null
                || !"Approved".equalsIgnoreCase(flight.getAirplane().getApprovalStatus())) {
            throw new IllegalArgumentException("Approve the airplane first before approving this flight.");
        }

        flight.setApprovalStatus("Approved");
        flightRepository.save(flight);
    }

    @Transactional
    public void rejectFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found."));
        flight.setApprovalStatus("Rejected");
        flightRepository.save(flight);
    }

    public long getPendingProviderCount() {
        return getPendingProviders().size();
    }

    public long getPendingAirplaneCount() {
        return getPendingAirplanes().size();
    }

    public long getPendingFlightCount() {
        return getPendingFlights().size();
    }

    private boolean isPending(String approvalStatus) {
        return approvalStatus == null || approvalStatus.trim().isEmpty() || "Pending".equalsIgnoreCase(approvalStatus);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
