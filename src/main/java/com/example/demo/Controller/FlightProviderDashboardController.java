package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Model.Airplane;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightProvider;
import com.example.demo.Model.Staff;
import com.example.demo.Service.FlightProviderDashboardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FlightProviderDashboardController {

    private final FlightProviderDashboardService flightProviderDashboardService;

    public FlightProviderDashboardController(FlightProviderDashboardService flightProviderDashboardService) {
        this.flightProviderDashboardService = flightProviderDashboardService;
    }

    @GetMapping("/flightProviderDashboard")
    public String showFlightProviderDashboard(HttpSession session, Model model) {

        FlightProvider loggedInFlightProvider =
                (FlightProvider) session.getAttribute("loggedInFlightProvider");

        if (loggedInFlightProvider == null) {
            return "redirect:/login";
        }

        List<Flight> flights =
                flightProviderDashboardService.getFlightsByProvider(loggedInFlightProvider);

        List<Airplane> airplanes =
                flightProviderDashboardService.getAirplanesByProvider(loggedInFlightProvider);

        List<Staff> staffAccounts =
                flightProviderDashboardService.getStaffByProvider(loggedInFlightProvider);

        List<Staff> pendingStaffAccounts =
                flightProviderDashboardService.getPendingStaffByProvider(loggedInFlightProvider);

        model.addAttribute("flightProvider", loggedInFlightProvider);
        model.addAttribute("providerName", loggedInFlightProvider.getCompanyName());

        model.addAttribute("flights", flights);
        model.addAttribute("airplanes", airplanes);
        model.addAttribute("staffAccounts", staffAccounts);
        model.addAttribute("pendingStaffAccounts", pendingStaffAccounts);

        model.addAttribute("totalFlights", flightProviderDashboardService.getTotalFlights(flights));
        model.addAttribute("activeFlights", flightProviderDashboardService.getActiveFlights(flights));
        model.addAttribute("totalSeats", flightProviderDashboardService.getTotalSeats(flights));
        model.addAttribute("todaysFlights", flightProviderDashboardService.getTodaysFlights(flights));
        model.addAttribute("pendingStaffCount", flightProviderDashboardService.getPendingStaffCount(loggedInFlightProvider));
        model.addAttribute("approvedStaffCount", flightProviderDashboardService.getApprovedStaffCount(loggedInFlightProvider));
        model.addAttribute("rejectedStaffCount", flightProviderDashboardService.getRejectedStaffCount(loggedInFlightProvider));

        return "FlightProviderDashboard";
    }

    @PostMapping("/flightProviderDashboard/createFlight")
    public String createFlight(
            @RequestParam Long airplaneId,
            @RequestParam String flightNumber,
            @RequestParam String origin,
            @RequestParam String destination,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate flightDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime flightTime,

            @RequestParam String flightStatus,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        FlightProvider loggedInFlightProvider =
                (FlightProvider) session.getAttribute("loggedInFlightProvider");

        if (loggedInFlightProvider == null) {
            return "redirect:/login";
        }

        try {
            flightProviderDashboardService.createFlight(
                    loggedInFlightProvider,
                    airplaneId,
                    flightNumber,
                    origin,
                    destination,
                    flightDate,
                    flightTime,
                    flightStatus
            );

            redirectAttributes.addFlashAttribute("successMessage", "Flight submitted for admin approval!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/flightProviderDashboard";
    }



    @PostMapping("/flightProviderDashboard/approveStaff/{staffId}")
    public String approveStaff(
            @PathVariable Long staffId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        FlightProvider loggedInFlightProvider =
                (FlightProvider) session.getAttribute("loggedInFlightProvider");

        if (loggedInFlightProvider == null) {
            return "redirect:/login";
        }

        try {
            flightProviderDashboardService.approveStaff(staffId, loggedInFlightProvider);
            redirectAttributes.addFlashAttribute("successMessage", "Staff account approved successfully. The staff member can now log in.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/flightProviderDashboard#staffApprovals";
    }

    @PostMapping("/flightProviderDashboard/rejectStaff/{staffId}")
    public String rejectStaff(
            @PathVariable Long staffId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        FlightProvider loggedInFlightProvider =
                (FlightProvider) session.getAttribute("loggedInFlightProvider");

        if (loggedInFlightProvider == null) {
            return "redirect:/login";
        }

        try {
            flightProviderDashboardService.rejectStaff(staffId, loggedInFlightProvider);
            redirectAttributes.addFlashAttribute("successMessage", "Staff account rejected.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/flightProviderDashboard#staffApprovals";
    }


    @GetMapping("/flightProviderDashboard/deleteFlight/{id}")
    public String deleteFlight(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        FlightProvider loggedInFlightProvider =
                (FlightProvider) session.getAttribute("loggedInFlightProvider");

        if (loggedInFlightProvider == null) {
            return "redirect:/login";
        }

        try {
            flightProviderDashboardService.deleteFlight(id, loggedInFlightProvider);
            redirectAttributes.addFlashAttribute("successMessage", "Flight deleted successfully!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/flightProviderDashboard";
    }
}