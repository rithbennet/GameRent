package com.rental.gamerent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage() {
        return "index"; // Corresponds to the Thymeleaf template 'index.html'
    }
}