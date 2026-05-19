package com.example.demo.Service;

import com.example.demo.Model.Booking;
import com.example.demo.Model.Flight;
import com.example.demo.Model.Staff;
import com.example.demo.Repository.BookingRepository;
import com.example.demo.Repository.FlightRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class StaffDashboardService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public StaffDashboardService(
            BookingRepository bookingRepository,
            FlightRepository flightRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Booking> getBookingsForStaff(Staff staff) {
        if (staff == null || staff.getFlightProvider() == null) {
            return bookingRepository.findAllByOrderByCreatedAtDesc();
        }

        return bookingRepository.findByFlightProviderOrderByCreatedAtDesc(staff.getFlightProvider());
    }

    public List<Booking> getPendingBookings() {
        return bookingRepository.findByBookingStatusOrderByCreatedAtAsc("Pending");
    }

    @Transactional
    public void approveBooking(Long bookingId) {
        approveBooking(bookingId, null);
    }

    @Transactional
    public void approveBooking(Long bookingId, Staff staff) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        validateStaffCanManageBooking(staff, booking);

        if (!"Pending".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalArgumentException("Only pending bookings can be approved.");
        }

        Flight flight = booking.getFlight();

        int remainingSeats = getRemainingSeats(flight);

        if (booking.getPassengerCount() > remainingSeats) {
            booking.setBookingStatus("Rejected");
            bookingRepository.save(booking);
            throw new IllegalArgumentException("Not enough seats. Booking was rejected.");
        }

        booking.setBookingStatus("Approved");
        booking.setPaymentStatus("Unpaid");
        booking.setApprovedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        updateFlightStatusIfFull(flight);
    }

    @Transactional
    public void rejectBooking(Long bookingId) {
        rejectBooking(bookingId, null);
    }

    @Transactional
    public void rejectBooking(Long bookingId, Staff staff) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        validateStaffCanManageBooking(staff, booking);

        if (!"Pending".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalArgumentException("Only pending bookings can be rejected.");
        }

        booking.setBookingStatus("Rejected");
        booking.setPaymentStatus("Unpaid");

        bookingRepository.save(booking);
    }

    public int getRemainingSeats(Flight flight) {
        Long reservedSeats = bookingRepository.countReservedSeats(
                flight,
                Arrays.asList("Approved", "Paid")
        );

        int usedSeats = reservedSeats == null ? 0 : reservedSeats.intValue();

        return flight.getMaximumSeatCapacity() - usedSeats;
    }

    private void updateFlightStatusIfFull(Flight flight) {
        int remainingSeats = getRemainingSeats(flight);

        if (remainingSeats <= 0) {
            flight.setFlightStatus("Full");
            flightRepository.save(flight);
        }
    }

    private void validateStaffCanManageBooking(Staff staff, Booking booking) {
        if (staff == null || staff.getFlightProvider() == null) {
            return;
        }

        if (booking.getFlight() == null || booking.getFlight().getFlightProvider() == null) {
            throw new IllegalArgumentException("This booking has no assigned flight provider.");
        }

        Long staffProviderId = staff.getFlightProvider().getFlightProviderId();
        Long bookingProviderId = booking.getFlight().getFlightProvider().getFlightProviderId();

        if (!staffProviderId.equals(bookingProviderId)) {
            throw new IllegalArgumentException("You can only manage bookings for your assigned flight provider company.");
        }
    }

    public long getPendingCount(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> "Pending".equalsIgnoreCase(b.getBookingStatus()))
                .count();
    }

    public long getApprovedCount(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> "Approved".equalsIgnoreCase(b.getBookingStatus()))
                .count();
    }

    public long getPaidCount(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> "Paid".equalsIgnoreCase(b.getBookingStatus()))
                .count();
    }
}