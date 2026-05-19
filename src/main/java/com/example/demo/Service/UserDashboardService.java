package com.example.demo.Service;

import com.example.demo.Model.Booking;
import com.example.demo.Model.Flight;
import com.example.demo.Model.User;
import com.example.demo.Repository.BookingRepository;
import com.example.demo.Repository.FlightRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class UserDashboardService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public UserDashboardService(
            FlightRepository flightRepository,
            BookingRepository bookingRepository
    ) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Flight> getAvailableApprovedFlights() {
        return flightRepository
                .findByFlightStatusIgnoreCaseAndApprovalStatusIgnoreCaseAndFlightProvider_ApprovalStatusIgnoreCaseAndAirplane_ApprovalStatusIgnoreCaseOrderByFlightDateAscFlightTimeAsc(
                        "Active",
                        "Approved",
                        "Approved",
                        "Approved"
                );
    }

    public List<Flight> searchAvailableFlights(String origin, String destination, LocalDate flightDate) {
        boolean hasOrigin = origin != null && !origin.trim().isEmpty();
        boolean hasDestination = destination != null && !destination.trim().isEmpty();
        boolean hasDate = flightDate != null;

        // All three provided — full search
        if (hasOrigin && hasDestination && hasDate) {
            return flightRepository
                    .findByOriginContainingIgnoreCaseAndDestinationContainingIgnoreCaseAndFlightDateAndFlightStatusIgnoreCaseAndApprovalStatusIgnoreCaseAndFlightProvider_ApprovalStatusIgnoreCaseAndAirplane_ApprovalStatusIgnoreCaseOrderByFlightTimeAsc(
                            origin.trim(),
                            destination.trim(),
                            flightDate,
                            "Active",
                            "Approved",
                            "Approved",
                            "Approved"
                    );
        }

        // Destination only (or destination + date, or destination + origin without date)
        if (hasDestination) {
            List<Flight> results = flightRepository
                    .findByDestinationContainingIgnoreCaseAndFlightStatusIgnoreCaseAndApprovalStatusIgnoreCaseAndFlightProvider_ApprovalStatusIgnoreCaseAndAirplane_ApprovalStatusIgnoreCaseOrderByFlightDateAscFlightTimeAsc(
                            destination.trim(),
                            "Active",
                            "Approved",
                            "Approved",
                            "Approved"
                    );
            // Filter by origin if provided
            if (hasOrigin) {
                final String o = origin.trim().toLowerCase();
                results = results.stream()
                        .filter(f -> f.getOrigin().toLowerCase().contains(o))
                        .collect(java.util.stream.Collectors.toList());
            }
            // Filter by date if provided
            if (hasDate) {
                results = results.stream()
                        .filter(f -> f.getFlightDate().equals(flightDate))
                        .collect(java.util.stream.Collectors.toList());
            }
            return results;
        }

        // Fallback: show all available flights
        return getAvailableApprovedFlights();
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public void bookFlight(User user, Long flightId, Integer passengerCount) {
        if (flightId == null) {
            throw new IllegalArgumentException("Flight is required.");
        }

        if (passengerCount == null || passengerCount <= 0) {
            throw new IllegalArgumentException("Passenger count must be greater than 0.");
        }

        Flight flight = flightRepository
                .findByIdAndFlightStatusIgnoreCaseAndApprovalStatusIgnoreCaseAndFlightProvider_ApprovalStatusIgnoreCaseAndAirplane_ApprovalStatusIgnoreCase(
                        flightId,
                        "Active",
                        "Approved",
                        "Approved",
                        "Approved"
                )
                .orElseThrow(() -> new IllegalArgumentException("Flight is not available."));

        int remainingSeats = getRemainingSeats(flight);

        if (passengerCount > remainingSeats) {
            throw new IllegalArgumentException("Not enough available seats for this flight.");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setPassengerCount(passengerCount);
        booking.setBookingStatus("Pending");
        booking.setPaymentStatus("Unpaid");
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }

    @Transactional
    public Booking payBooking(User user, Long bookingId) {
        Booking booking = bookingRepository
                .findByBookingIdAndUser(bookingId, user)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        if (!"Approved".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalArgumentException("Only approved bookings can be paid.");
        }

        booking.setBookingStatus("Paid");
        booking.setPaymentStatus("Paid");
        booking.setPaidAt(LocalDateTime.now());
        booking.setReceiptNumber("REC-" + booking.getBookingId() + "-" + System.currentTimeMillis());

        return bookingRepository.save(booking);
    }

    public Booking getUserReceipt(User user, Long bookingId) {
        Booking booking = bookingRepository
                .findByBookingIdAndUser(bookingId, user)
                .orElseThrow(() -> new IllegalArgumentException("Receipt not found."));

        if (!"Paid".equalsIgnoreCase(booking.getPaymentStatus())) {
            throw new IllegalArgumentException("Receipt is only available after payment.");
        }

        return booking;
    }

    public int getRemainingSeats(Flight flight) {
        Long reservedSeats = bookingRepository.countReservedSeats(
                flight,
                Arrays.asList("Approved", "Paid")
        );

        int usedSeats = reservedSeats == null ? 0 : reservedSeats.intValue();

        return flight.getMaximumSeatCapacity() - usedSeats;
    }
}