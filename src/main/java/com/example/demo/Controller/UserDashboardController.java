package com.example.demo.Controller;

import com.example.demo.Model.Booking;
import com.example.demo.Model.Flight;
import com.example.demo.Model.User;
import com.example.demo.Service.UserDashboardService;

import jakarta.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class UserDashboardController {

    private final UserDashboardService userDashboardService;

    public UserDashboardController(UserDashboardService userDashboardService) {
        this.userDashboardService = userDashboardService;
    }

    @GetMapping("/userDashboard")
    public String showUserDashboard(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate flightDate,

            HttpSession session,
            Model model
    ) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedInUser);
        model.addAttribute("userName", loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
        model.addAttribute("myBookings", userDashboardService.getUserBookings(loggedInUser));

        boolean hasSearchInput = hasText(origin) || hasText(destination) || flightDate != null;

        try {
            List<Flight> availableFlights;

            if (hasSearchInput) {
                availableFlights = userDashboardService.searchAvailableFlights(origin, destination, flightDate);
            } else {
                availableFlights = userDashboardService.getAvailableApprovedFlights();
            }

            model.addAttribute("availableFlights", availableFlights);

        } catch (IllegalArgumentException e) {
            model.addAttribute("availableFlights", userDashboardService.getAvailableApprovedFlights());
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("origin", origin);
        model.addAttribute("destination", destination);
        model.addAttribute("flightDate", flightDate);

        return "UserDashboard";
    }

    @PostMapping("/userDashboard/bookFlight")
    public String bookFlight(
            @RequestParam Long flightId,
            @RequestParam Integer passengerCount,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            userDashboardService.bookFlight(loggedInUser, flightId, passengerCount);
            redirectAttributes.addFlashAttribute("successMessage", "Booking submitted. Please wait for staff approval.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/userDashboard";
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @PostMapping("/userDashboard/payBooking/{bookingId}")
    public String payBooking(
            @PathVariable Long bookingId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            userDashboardService.payBooking(loggedInUser, bookingId);
            redirectAttributes.addFlashAttribute("successMessage", "Payment successful. Receipt is now available.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/userDashboard";
    }

    @GetMapping("/userDashboard/receipt/{bookingId}")
    public String viewReceipt(
            @PathVariable Long bookingId,
            HttpSession session,
            Model model
    ) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Booking booking = userDashboardService.getUserReceipt(loggedInUser, bookingId);

        model.addAttribute("booking", booking);
        model.addAttribute("user", loggedInUser);

        return "UserReceipt";
    }
}