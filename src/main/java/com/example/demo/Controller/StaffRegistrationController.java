package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Model.Staff;
import com.example.demo.Service.StaffRegistrationService;

@Controller
public class StaffRegistrationController {

    private final StaffRegistrationService staffRegistrationService;

    public StaffRegistrationController(StaffRegistrationService staffRegistrationService) {
        this.staffRegistrationService = staffRegistrationService;
    }

    @GetMapping({"/staffRegistration", "/staffRegister", "/staff/register"})
    public String showStaffRegistrationPage(Model model) {
        model.addAttribute("staff", new Staff());
        model.addAttribute("flightProviders", staffRegistrationService.getApprovedFlightProviders());
        return "StaffRegister";
    }

    @PostMapping("/staffRegistration")
    public String registerStaff(
            @ModelAttribute("staff") Staff staff,
            @RequestParam(required = false) Long flightProviderId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            staffRegistrationService.registerStaff(
                    staff.getFirstName(),
                    staff.getLastName(),
                    staff.getEmailAddress(),
                    staff.getContactNumber(),
                    staff.getStaffRole(),
                    staff.getPassword(),
                    flightProviderId
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Staff account submitted successfully. Please wait for the selected flight provider to approve it before logging in."
            );

            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("staff", staff);
            model.addAttribute("flightProviders", staffRegistrationService.getApprovedFlightProviders());
            return "StaffRegister";
        }
    }
}
