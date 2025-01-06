package com.rental.gamerent.controller;

import com.rental.gamerent.dto.RegistrationDTO;
import com.rental.gamerent.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO()); // Add DTO to the model
        return "register";
    }

    @PostMapping("/register")
    public String register(RegistrationDTO registrationDTO, Model model) {
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "register"; // Show registration page with error
        }

        userService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword(), registrationDTO.getEmail());
        model.addAttribute("success", "Registration successful");
        return "redirect:/login"; // Redirect to login page after successful registration
    }
}