package com.example.demo.Repository;

import com.example.demo.Model.Booking;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightProvider;
import com.example.demo.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserOrderByCreatedAtDesc(User user);

    List<Booking> findAllByOrderByCreatedAtDesc();

    @Query("SELECT b FROM Booking b WHERE b.flight.flightProvider = :flightProvider ORDER BY b.createdAt DESC")
    List<Booking> findByFlightProviderOrderByCreatedAtDesc(@Param("flightProvider") FlightProvider flightProvider);

    List<Booking> findByBookingStatusOrderByCreatedAtAsc(String bookingStatus);

    Optional<Booking> findByBookingIdAndUser(Long bookingId, User user);

    @Query("SELECT SUM(b.passengerCount) FROM Booking b WHERE b.flight = :flight AND b.bookingStatus IN :statuses")
    Long countReservedSeats(
            @Param("flight") Flight flight,
            @Param("statuses") List<String> statuses
    );
}