package com.rental.gamerent.controller;

import com.rental.gamerent.dto.RegistrationDTO;
import jakarta.servlet.http.HttpServletRequest;
import com.rental.gamerent.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/register")
    public String register(RegistrationDTO registrationDTO, Model model, HttpServletRequest httpServletRequest) {
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "login"; // Show registration page with error
        }

        userService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword(), registrationDTO.getEmail());

         // Auto-login after successful registration
         try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registrationDTO.getUsername(), registrationDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpServletRequest.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            logger.info("User {} successfully authenticated", registrationDTO.getUsername());
        } catch (Exception e) {
            logger.error("Authentication failed for user {}", registrationDTO.getUsername(), e);
            model.addAttribute("error", "Authentication failed");
            return "login"; // Show login page with error
        }

        model.addAttribute("success", "Registration successful");
        return "redirect:/games";
     }


}