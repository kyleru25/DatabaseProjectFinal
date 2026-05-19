package com.example.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Model.Booking;
import com.example.demo.Model.Staff;
import com.example.demo.Service.StaffDashboardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StaffDashboardController {

    private final StaffDashboardService staffDashboardService;

    public StaffDashboardController(StaffDashboardService staffDashboardService) {
        this.staffDashboardService = staffDashboardService;
    }

    @GetMapping("/staffDashboard")
    public String showStaffDashboard(HttpSession session, Model model) {

        Staff loggedInStaff = (Staff) session.getAttribute("loggedInStaff");

        if (loggedInStaff == null) {
            return "redirect:/login";
        }

        List<Booking> bookings = staffDashboardService.getBookingsForStaff(loggedInStaff);

        model.addAttribute("staff", loggedInStaff);
        model.addAttribute("staffName", loggedInStaff.getFirstName() + " " + loggedInStaff.getLastName());
        model.addAttribute("staffRole", loggedInStaff.getStaffRole());
        model.addAttribute(
                "providerName",
                loggedInStaff.getFlightProvider() != null ? loggedInStaff.getFlightProvider().getCompanyName() : "All Providers"
        );

        model.addAttribute("bookings", bookings);
        model.addAttribute("pendingCount", staffDashboardService.getPendingCount(bookings));
        model.addAttribute("approvedCount", staffDashboardService.getApprovedCount(bookings));
        model.addAttribute("paidCount", staffDashboardService.getPaidCount(bookings));

        return "StaffDashboard";
    }

    @PostMapping("/staffDashboard/approveBooking/{bookingId}")
    public String approveBooking(
            @PathVariable Long bookingId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Staff loggedInStaff = (Staff) session.getAttribute("loggedInStaff");

        if (loggedInStaff == null) {
            return "redirect:/login";
        }

        try {
            staffDashboardService.approveBooking(bookingId, loggedInStaff);
            redirectAttributes.addFlashAttribute("successMessage", "Booking approved successfully.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/staffDashboard";
    }

    @PostMapping("/staffDashboard/rejectBooking/{bookingId}")
    public String rejectBooking(
            @PathVariable Long bookingId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Staff loggedInStaff = (Staff) session.getAttribute("loggedInStaff");

        if (loggedInStaff == null) {
            return "redirect:/login";
        }

        try {
            staffDashboardService.rejectBooking(bookingId, loggedInStaff);
            redirectAttributes.addFlashAttribute("successMessage", "Booking rejected.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/staffDashboard";
    }
}