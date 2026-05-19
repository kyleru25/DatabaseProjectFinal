package com.example.demo.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Model.Flight;
import com.example.demo.Service.UserDashboardService;

@Controller
public class IndexController {

    private final UserDashboardService userDashboardService;

    public IndexController(UserDashboardService userDashboardService) {
        this.userDashboardService = userDashboardService;
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("availableFlights", userDashboardService.getAvailableApprovedFlights());
        return "Index";
    }

    @GetMapping("/flights/search")
    public String searchFlights(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false, name = "dest") String destination,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate depart,

            Model model
    ) {

        List<Flight> availableFlights;

        boolean hasSearchInput =
                (origin != null && !origin.trim().isEmpty())
                        || (destination != null && !destination.trim().isEmpty())
                        || depart != null;

        try {

            if (hasSearchInput) {
                availableFlights = userDashboardService.searchAvailableFlights(
                        origin,
                        destination,
                        depart
                );
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
        model.addAttribute("depart", depart);
        model.addAttribute("activeTab", "flight");
        model.addAttribute("searchPerformed", hasSearchInput);

        return "Index";
    }
}
