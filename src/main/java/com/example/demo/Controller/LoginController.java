package com.example.demo.Controller;

import com.example.demo.Model.FlightProvider;
import com.example.demo.Model.LoginResult;
import com.example.demo.Model.Staff;
import com.example.demo.Model.User;
import com.example.demo.Service.UnifiedLoginService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UnifiedLoginService unifiedLoginService;

    public LoginController(UnifiedLoginService unifiedLoginService) {
        this.unifiedLoginService = unifiedLoginService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        if (session.getAttribute("loggedInAdmin") != null) {
            return "redirect:/adminDashboard";
        }
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/userDashboard";
        }
        if (session.getAttribute("loggedInStaff") != null) {
            return "redirect:/staffDashboard";
        }
        if (session.getAttribute("loggedInFlightProvider") != null) {
            return "redirect:/flightProviderDashboard";
        }

        return "Login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String emailAddress,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        try {
            LoginResult loginResult = unifiedLoginService.login(emailAddress, password);
            session.removeAttribute("loggedInAdmin");
            session.removeAttribute("loggedInUser");
            session.removeAttribute("loggedInStaff");
            session.removeAttribute("loggedInFlightProvider");
            session.setMaxInactiveInterval(30 * 60);

            if ("ADMIN".equalsIgnoreCase(loginResult.getRole())) {
                session.setAttribute("loggedInAdmin", loginResult.getUser());
                return "redirect:/adminDashboard";
            }

            if ("USER".equalsIgnoreCase(loginResult.getRole())) {
                session.setAttribute("loggedInUser", (User) loginResult.getUser());
                return "redirect:/userDashboard";
            }

            if ("STAFF".equalsIgnoreCase(loginResult.getRole())) {
                session.setAttribute("loggedInStaff", (Staff) loginResult.getUser());
                return "redirect:/staffDashboard";
            }

            if ("FLIGHT_PROVIDER".equalsIgnoreCase(loginResult.getRole())) {
                session.setAttribute("loggedInFlightProvider", (FlightProvider) loginResult.getUser());
                return "redirect:/flightProviderDashboard";
            }

            model.addAttribute("loginError", "Unsupported account type.");
            model.addAttribute("emailAddress", emailAddress);
            return "Login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("loginError", e.getMessage());
            model.addAttribute("emailAddress", emailAddress);
            return "Login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping({"/staffLogin", "/flightProviderLogin", "/provider/login"})
    public String legacyLoginRedirect() {
        return "redirect:/login";
    }

    @GetMapping({"/staffLogout", "/flightProviderLogout"})
    public String legacyLogoutRedirect(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
