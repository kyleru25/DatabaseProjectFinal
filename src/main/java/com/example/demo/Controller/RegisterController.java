package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.RegisterService;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class RegisterController {
    @Autowired
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }
    
    @GetMapping("/register")
    public String showRegisterFrame(Model model) {
        model.addAttribute("user", new User());
        return "Register";
    }

    @PostMapping("/register")
    public String showRegisterFrame(@ModelAttribute User user, Model model) throws Exception{
        try {
            registerService.userRegistration(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmailAddress(),
                        user.getPhoneNumber(),
                        user.getPassword()
            );
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("user", user);
            return "Register";
        }
    }
    
}
