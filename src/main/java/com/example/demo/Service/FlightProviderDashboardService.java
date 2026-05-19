package com.example.demo.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Model.Airplane;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightProvider;
import com.example.demo.Model.Staff;
import com.example.demo.Repository.AirplaneRepository;
import com.example.demo.Repository.FlightRepository;
import com.example.demo.Repository.StaffRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlightProviderDashboardService {

    private final FlightRepository flightRepository;
    private final AirplaneRepository airplaneRepository;
    private final StaffRepository staffRepository;

    public FlightProviderDashboardService(
            FlightRepository flightRepository,
            AirplaneRepository airplaneRepository,
            StaffRepository staffRepository
    ) {
        this.flightRepository = flightRepository;
        this.airplaneRepository = airplaneRepository;
        this.staffRepository = staffRepository;
    }

    public List<Flight> getFlightsByProvider(FlightProvider flightProvider) {
        return flightRepository.findByFlightProviderOrderByFlightDateAscFlightTimeAsc(flightProvider);
    }

    public List<Airplane> getAirplanesByProvider(FlightProvider flightProvider) {
        return airplaneRepository.findByFlightProviderAndApprovalStatusIgnoreCaseOrderByAirplaneNameAsc(flightProvider, "Approved");
    }

    public List<Staff> getStaffByProvider(FlightProvider flightProvider) {
        return staffRepository.findByFlightProviderOrderByFirstNameAscLastNameAsc(flightProvider);
    }

    public List<Staff> getPendingStaffByProvider(FlightProvider flightProvider) {
        return staffRepository.findByFlightProviderAndStaffStatusIgnoreCaseOrderByFirstNameAscLastNameAsc(
                flightProvider,
                "Pending"
        );
    }

    public void createFlight(
            FlightProvider flightProvider,
            Long airplaneId,
            String flightNumber,
            String origin,
            String destination,
            LocalDate flightDate,
            LocalTime flightTime,
            String flightStatus
    ) {
        if (airplaneId == null) {
            throw new IllegalArgumentException("Please select an airplane.");
        }

        Airplane airplane = airplaneRepository
                .findByIdAndFlightProvider(airplaneId, flightProvider)
                .orElseThrow(() -> new IllegalArgumentException("Selected airplane was not found."));

        if (!"Approved".equalsIgnoreCase(airplane.getApprovalStatus())) {
            throw new IllegalArgumentException("Selected airplane is still waiting for admin approval.");
        }

        if (!"Available".equalsIgnoreCase(airplane.getAirplaneStatus())) {
            throw new IllegalArgumentException("Selected airplane is not available.");
        }

        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number is required.");
        }

        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin is required.");
        }

        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination is required.");
        }

        if (origin.trim().equalsIgnoreCase(destination.trim())) {
            throw new IllegalArgumentException("Origin and destination cannot be the same.");
        }

        if (flightDate == null) {
            throw new IllegalArgumentException("Flight date is required.");
        }

        if (flightTime == null) {
            throw new IllegalArgumentException("Flight time is required.");
        }

        boolean flightNumberExists =
                flightRepository.existsByFlightNumberIgnoreCaseAndFlightProvider(
                        flightNumber.trim(),
                        flightProvider
                );

        if (flightNumberExists) {
            throw new IllegalArgumentException("Flight number already exists for this provider.");
        }

        Flight flight = new Flight();

        flight.setFlightProvider(flightProvider);
        flight.setAirplane(airplane);
        flight.setFlightNumber(flightNumber.trim());
        flight.setOrigin(origin.trim());
        flight.setDestination(destination.trim());
        flight.setMaximumSeatCapacity(airplane.getMaximumSeatCapacity());
        flight.setFlightDate(flightDate);
        flight.setFlightTime(flightTime);
        flight.setFlightStatus(flightStatus == null || flightStatus.trim().isEmpty() ? "Active" : flightStatus.trim());
        flight.setApprovalStatus("Pending");

        flightRepository.save(flight);
    }

    public int getTotalFlights(List<Flight> flights) {
        return flights.size();
    }

    public long getActiveFlights(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> "Active".equalsIgnoreCase(flight.getFlightStatus()) && "Approved".equalsIgnoreCase(flight.getApprovalStatus()))
                .count();
    }

    public int getTotalSeats(List<Flight> flights) {
        return flights.stream()
                .mapToInt(Flight::getMaximumSeatCapacity)
                .sum();
    }

    public long getTodaysFlights(List<Flight> flights) {
        LocalDate today = LocalDate.now();

        return flights.stream()
                .filter(flight -> today.equals(flight.getFlightDate()))
                .count();
    }

    public long getPendingStaffCount(FlightProvider flightProvider) {
        return staffRepository.countByFlightProviderAndStaffStatusIgnoreCase(flightProvider, "Pending");
    }

    public long getApprovedStaffCount(FlightProvider flightProvider) {
        return staffRepository.countByFlightProviderAndStaffStatusIgnoreCase(flightProvider, "Active");
    }

    public long getRejectedStaffCount(FlightProvider flightProvider) {
        return staffRepository.countByFlightProviderAndStaffStatusIgnoreCase(flightProvider, "Rejected");
    }

    @Transactional
    public void approveStaff(Long staffId, FlightProvider flightProvider) {
        Staff staff = staffRepository
                .findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff account not found."));

        if (staff.getFlightProvider() == null
                || !staff.getFlightProvider().getFlightProviderId().equals(flightProvider.getFlightProviderId())) {
            throw new IllegalArgumentException("This staff account does not belong to your flight provider company.");
        }

        if (!"Pending".equalsIgnoreCase(staff.getStaffStatus())) {
            throw new IllegalArgumentException("Only pending staff accounts can be approved.");
        }

        staff.setStaffStatus("Active");
        staffRepository.save(staff);
    }

    @Transactional
    public void rejectStaff(Long staffId, FlightProvider flightProvider) {
        Staff staff = staffRepository
                .findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff account not found."));

        if (staff.getFlightProvider() == null
                || !staff.getFlightProvider().getFlightProviderId().equals(flightProvider.getFlightProviderId())) {
            throw new IllegalArgumentException("This staff account does not belong to your flight provider company.");
        }

        if (!"Pending".equalsIgnoreCase(staff.getStaffStatus())) {
            throw new IllegalArgumentException("Only pending staff accounts can be rejected.");
        }

        staff.setStaffStatus("Rejected");
        staffRepository.save(staff);
    }

    public void deleteFlight(Long id, FlightProvider flightProvider) {
    Flight flight = flightRepository
            .findByIdAndFlightProvider(id, flightProvider)
            .orElseThrow(() -> new IllegalArgumentException("Flight not found."));

    flightRepository.delete(flight);
    }
}