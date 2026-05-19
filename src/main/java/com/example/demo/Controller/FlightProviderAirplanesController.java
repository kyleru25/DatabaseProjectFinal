package com.example.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Model.Airplane;
import com.example.demo.Model.FlightProvider;
import com.example.demo.Service.FlightProviderAirplanesService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FlightProviderAirplanesController {

    private final FlightProviderAirplanesService flightProviderAirplanesService;

    public FlightProviderAirplanesController(FlightProviderAirplanesService flightProviderAirplanesService) {
        this.flightProviderAirplanesService = flightProviderAirplanesService;
    }

    @GetMapping("/flightProviderAirplanes")
    public String showFlightProviderAirplanes(HttpSession session, Model model) {

        FlightProvider loggedInFlightProvider =
                (FlightProvider) session.getAttribute("loggedInFlightProvider");

        if (loggedInFlightProvider == null) {
            return "redirect:/login";
        }

        List<Airplane> airplanes =
                flightProviderAirplanesService.getAirplanesByProvider(loggedInFlightProvider);

        model.addAttribute("flightProvider", loggedInFlightProvider);
        model.addAttribute("providerName", loggedInFlightProvider.getCompanyName());

        model.addAttribute("airplanes", airplanes);
        model.addAttribute("totalAirplanes", flightProviderAirplanesService.getTotalAirplanes(airplanes));
        model.addAttribute("availableAirplanes", flightProviderAirplanesService.getAvailableAirplanes(airplanes));
        model.addAttribute("totalSeatCapacity", flightProviderAirplanesService.getTotalSeatCapacity(airplanes));

        return "FlightProviderAirplanes";
    }

    @PostMapping("/flightProviderAirplanes/addAirplane")
    public String addAirplane(
            @RequestParam String airplaneName,
            @RequestParam String airplaneModel,
            @RequestParam String airplaneCode,
            @RequestParam Integer maximumSeatCapacity,
            @RequestParam String airplaneStatus,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        FlightProvider loggedInFlightProvider =
                (FlightProvider) session.getAttribute("loggedInFlightProvider");

        if (loggedInFlightProvider == null) {
            return "redirect:/login";
        }

        try {
            flightProviderAirplanesService.addAirplane(
                    loggedInFlightProvider,
                    airplaneName,
                    airplaneModel,
                    airplaneCode,
                    maximumSeatCapacity,
                    airplaneStatus
            );

            redirectAttributes.addFlashAttribute("successMessage", "Airplane submitted for admin approval!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/flightProviderAirplanes";
    }

    @GetMapping("/flightProviderAirplanes/deleteAirplane/{id}")
    public String deleteAirplane(
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
            flightProviderAirplanesService.deleteAirplane(id, loggedInFlightProvider);
            redirectAttributes.addFlashAttribute("successMessage", "Airplane deleted successfully!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/flightProviderAirplanes";
    }
}