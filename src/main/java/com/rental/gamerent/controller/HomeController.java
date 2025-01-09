package com.rental.gamerent.controller;

import com.rental.gamerent.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping({"/home", "/"})
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("Authentication object: {}", auth);
            if (auth.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
                logger.info("UserPrincipal object: {}", userPrincipal);

                model.addAttribute("id", userPrincipal.getId());
                model.addAttribute("username", userPrincipal.getUsername());
                model.addAttribute("role", userPrincipal.getRole());
                model.addAttribute("email", userPrincipal.getEmail());
            } else {
                logger.warn("Principal is not an instance of UserPrincipal");
            }
        } else {
            logger.warn("Authentication object is null");
        }
        return "home";
    }
}