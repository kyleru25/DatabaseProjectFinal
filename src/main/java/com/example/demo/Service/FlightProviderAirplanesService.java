package com.example.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Model.Airplane;
import com.example.demo.Model.FlightProvider;
import com.example.demo.Repository.AirplaneRepository;

@Service
public class FlightProviderAirplanesService {

    private final AirplaneRepository airplaneRepository;

    public FlightProviderAirplanesService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public List<Airplane> getAirplanesByProvider(FlightProvider flightProvider) {
        return airplaneRepository.findByFlightProviderOrderByAirplaneNameAsc(flightProvider);
    }

    public void addAirplane(
            FlightProvider flightProvider,
            String airplaneName,
            String airplaneModel,
            String airplaneCode,
            Integer maximumSeatCapacity,
            String airplaneStatus
    ) {
        if (airplaneName == null || airplaneName.trim().isEmpty()) {
            throw new IllegalArgumentException("Airplane name is required.");
        }

        if (airplaneModel == null || airplaneModel.trim().isEmpty()) {
            throw new IllegalArgumentException("Airplane model is required.");
        }

        if (airplaneCode == null || airplaneCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Airplane code is required.");
        }

        if (maximumSeatCapacity == null || maximumSeatCapacity <= 0) {
            throw new IllegalArgumentException("Maximum seat capacity must be greater than 0.");
        }

        if (airplaneStatus == null || airplaneStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Airplane status is required.");
        }

        boolean airplaneCodeExists =
                airplaneRepository.existsByAirplaneCodeIgnoreCaseAndFlightProvider(
                        airplaneCode.trim(),
                        flightProvider
                );

        if (airplaneCodeExists) {
            throw new IllegalArgumentException("Airplane code already exists for this provider.");
        }

        Airplane airplane = new Airplane();

        airplane.setAirplaneName(airplaneName.trim());
        airplane.setAirplaneModel(airplaneModel.trim());
        airplane.setAirplaneCode(airplaneCode.trim());
        airplane.setMaximumSeatCapacity(maximumSeatCapacity);
        airplane.setAirplaneStatus(airplaneStatus.trim());
        airplane.setApprovalStatus("Pending");
        airplane.setFlightProvider(flightProvider);

        airplaneRepository.save(airplane);
    }

    public void deleteAirplane(Long id, FlightProvider flightProvider) {
        Airplane airplane = airplaneRepository
                .findByIdAndFlightProvider(id, flightProvider)
                .orElseThrow(() -> new IllegalArgumentException("Airplane not found."));

        airplaneRepository.delete(airplane);
    }

    public int getTotalAirplanes(List<Airplane> airplanes) {
        return airplanes.size();
    }

    public long getAvailableAirplanes(List<Airplane> airplanes) {
        return airplanes.stream()
                .filter(airplane -> "Available".equalsIgnoreCase(airplane.getAirplaneStatus()))
                .count();
    }

    public int getTotalSeatCapacity(List<Airplane> airplanes) {
        return airplanes.stream()
                .mapToInt(Airplane::getMaximumSeatCapacity)
                .sum();
    }
}