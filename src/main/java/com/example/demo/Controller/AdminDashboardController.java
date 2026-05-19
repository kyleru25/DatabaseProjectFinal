package com.example.demo.Controller;

import com.example.demo.Service.AdminDashboardService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/adminDashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        Object loggedInAdmin = session.getAttribute("loggedInAdmin");

        if (loggedInAdmin == null) {
            return "redirect:/login";
        }

        model.addAttribute("adminName", loggedInAdmin.toString());
        model.addAttribute("pendingProviders", adminDashboardService.getPendingProviders());
        model.addAttribute("pendingAirplanes", adminDashboardService.getPendingAirplanes());
        model.addAttribute("pendingFlights", adminDashboardService.getPendingFlights());
        model.addAttribute("pendingProviderCount", adminDashboardService.getPendingProviderCount());
        model.addAttribute("pendingAirplaneCount", adminDashboardService.getPendingAirplaneCount());
        model.addAttribute("pendingFlightCount", adminDashboardService.getPendingFlightCount());

        return "AdminDashboard";
    }

    @PostMapping("/adminDashboard/providers/{id}/approve")
    public String approveProvider(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }

        try {
            adminDashboardService.approveProvider(id);
            redirectAttributes.addFlashAttribute("successMessage", "Flight provider approved successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/adminDashboard";
    }

    @PostMapping("/adminDashboard/providers/{id}/reject")
    public String rejectProvider(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }

        try {
            adminDashboardService.rejectProvider(id);
            redirectAttributes.addFlashAttribute("successMessage", "Flight provider rejected.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/adminDashboard";
    }

    @PostMapping("/adminDashboard/airplanes/{id}/approve")
    public String approveAirplane(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }

        try {
            adminDashboardService.approveAirplane(id);
            redirectAttributes.addFlashAttribute("successMessage", "Airplane approved successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/adminDashboard";
    }

    @PostMapping("/adminDashboard/airplanes/{id}/reject")
    public String rejectAirplane(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }

        try {
            adminDashboardService.rejectAirplane(id);
            redirectAttributes.addFlashAttribute("successMessage", "Airplane rejected.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/adminDashboard";
    }

    @PostMapping("/adminDashboard/flights/{id}/approve")
    public String approveFlight(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }

        try {
            adminDashboardService.approveFlight(id);
            redirectAttributes.addFlashAttribute("successMessage", "Flight approved and now visible to users.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/adminDashboard";
    }

    @PostMapping("/adminDashboard/flights/{id}/reject")
    public String rejectFlight(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }

        try {
            adminDashboardService.rejectFlight(id);
            redirectAttributes.addFlashAttribute("successMessage", "Flight rejected.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/adminDashboard";
    }
}
