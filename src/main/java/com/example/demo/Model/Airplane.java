package com.example.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String airplaneName;
    private String airplaneModel;
    private String airplaneCode;
    private Integer maximumSeatCapacity;
    private String airplaneStatus;
    private String approvalStatus;

    @ManyToOne
    @JoinColumn(name = "flight_provider_id")
    private FlightProvider flightProvider;

    public Airplane() {
    }

    public Airplane(
            String airplaneName,
            String airplaneModel,
            String airplaneCode,
            Integer maximumSeatCapacity,
            String airplaneStatus,
            FlightProvider flightProvider
    ) {
        this.airplaneName = airplaneName;
        this.airplaneModel = airplaneModel;
        this.airplaneCode = airplaneCode;
        this.maximumSeatCapacity = maximumSeatCapacity;
        this.airplaneStatus = airplaneStatus;
        this.approvalStatus = "Pending";
        this.flightProvider = flightProvider;
    }

    public Long getId() {
        return id;
    }

    public String getAirplaneName() {
        return airplaneName;
    }

    public void setAirplaneName(String airplaneName) {
        this.airplaneName = airplaneName;
    }

    public String getAirplaneModel() {
        return airplaneModel;
    }

    public void setAirplaneModel(String airplaneModel) {
        this.airplaneModel = airplaneModel;
    }

    public String getAirplaneCode() {
        return airplaneCode;
    }

    public void setAirplaneCode(String airplaneCode) {
        this.airplaneCode = airplaneCode;
    }

    public Integer getMaximumSeatCapacity() {
        return maximumSeatCapacity;
    }

    public void setMaximumSeatCapacity(Integer maximumSeatCapacity) {
        this.maximumSeatCapacity = maximumSeatCapacity;
    }

    public String getAirplaneStatus() {
        return airplaneStatus;
    }

    public void setAirplaneStatus(String airplaneStatus) {
        this.airplaneStatus = airplaneStatus;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public FlightProvider getFlightProvider() {
        return flightProvider;
    }

    public void setFlightProvider(FlightProvider flightProvider) {
        this.flightProvider = flightProvider;
    }
}
