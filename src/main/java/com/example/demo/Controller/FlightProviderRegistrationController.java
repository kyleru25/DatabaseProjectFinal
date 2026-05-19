package com.example.demo.Controller;

import com.example.demo.Model.FlightProvider;
import com.example.demo.Service.FlightProviderRegistrationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class FlightProviderRegistrationController {
    
    @Autowired
    private FlightProviderRegistrationService flightProviderRegistrationService;

    public FlightProviderRegistrationController(FlightProviderRegistrationService flightProviderRegistrationService) {
        this.flightProviderRegistrationService = flightProviderRegistrationService;
    }

    
    @GetMapping("/flightProviderRegistration")
    public String showFlightProviderRegistrationFrame(Model model) {
        model.addAttribute("flightProvider", new FlightProvider());
        return "FlightProviderRegister";
    }

    @PostMapping("/flightProviderRegistration")
    public String showFlightProviderRegistrationFrame(@ModelAttribute FlightProvider flightProvider, Model model, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            flightProviderRegistrationService.registerFlightProvider(flightProvider.getCompanyName(), flightProvider.getRepresentativeFirstName(), flightProvider.getRepresentativeLastName(), flightProvider.getEmailAddress(), flightProvider.getContactNumber(), flightProvider.getBusinessPermitNumber(), flightProvider.getProviderType(), flightProvider.getPassword(), flightProvider.getCompanyAddress());
            redirectAttributes.addFlashAttribute("successMessage", "Your flight provider account was submitted. Please wait for admin approval before logging in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "FlightProviderRegister";
        }
    }
}
