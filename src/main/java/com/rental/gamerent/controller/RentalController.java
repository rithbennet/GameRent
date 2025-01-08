package com.rental.gamerent.controller;

import com.rental.gamerent.model.Rental;
import com.rental.gamerent.service.RentalService;
import com.rental.gamerent.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final GameService gameService;

    @Autowired
    public RentalController(RentalService rentalService, GameService gameService) {
        this.rentalService = rentalService;
        this.gameService = gameService;
    }

    @GetMapping("/form")
    public String showRentalForm(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "rental-form";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRental(
            @RequestParam("gameId") Long gameId,
            @RequestParam("userId") Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            Rental rental = rentalService.createRental(gameId, userId, startDate, endDate);
            return ResponseEntity.ok(rental);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Rental>> getActiveRentals(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(rentalService.getActiveRentals(userId));
    }

    @GetMapping("/history") //hardcoded for now to test
    public String getRentalHistory(Model model) {
        // Hardcode userId=2 for testing since that's what's in your database
        Long testUserId = 2L;

        try {
            List<Rental> activeRentals = rentalService.getActiveRentals(testUserId);
            List<Rental> previousRentals = rentalService.getPreviousRentals(testUserId);

            // Add debug logging
            System.out.println("Active rentals found: " + (activeRentals != null ? activeRentals.size() : "null"));
            System.out.println("Previous rentals found: " + (previousRentals != null ? previousRentals.size() : "null"));

            model.addAttribute("activeRentals", activeRentals);
            model.addAttribute("previousRentals", previousRentals);

            return "rental-history";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load rental history: " + e.getMessage());
            return "rental-history";
        }
    }
//    @GetMapping("/history")
//    public String getRentalHistory(@RequestParam("userId") Long userId, Model model) {
//        try {
//            List<Rental> activeRentals = rentalService.getActiveRentals(userId);
//            List<Rental> previousRentals = rentalService.getPreviousRentals(userId);
//
//            model.addAttribute("activeRentals", activeRentals);
//            model.addAttribute("previousRentals", previousRentals);
//
//            return "rental-history";  // Make sure this matches your template name
//        } catch (Exception e) {
//            // Add error attribute if something goes wrong
//            model.addAttribute("error", "Failed to load rental history");
//            return "error";  // You should have an error template
//        }
//    }

    @PostMapping("/{rentalId}/return")
    public ResponseEntity<?> returnRental(@PathVariable Long rentalId) {
        try {
            Rental rental = rentalService.returnRental(rentalId);
            return ResponseEntity.ok(rental);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception here
        return ResponseEntity.internalServerError().body("An unexpected error occurred");
    }
}