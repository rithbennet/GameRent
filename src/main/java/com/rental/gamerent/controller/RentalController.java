package com.rental.gamerent.controller;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Rental;
import com.rental.gamerent.service.RentalService;
import com.rental.gamerent.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @PostMapping("/create")
    public String createRental(
            @RequestParam("gameId") Long gameId,
            @RequestParam("userId") Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        try {
            Rental rental = rentalService.createRental(gameId, userId, startDate, endDate);
            model.addAttribute("rental", rental);
            return "rental-success"; // Return the success view
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "rental-form"; // Return to form with error
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "rental-form"; // Return to form with error
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred");
            return "rental-form"; // Return to form with error
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Rental>> getActiveRentals(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(rentalService.getActiveRentals(userId));
    }

    @GetMapping("/history") // hardcoded for now to test
    public String getRentalHistory(Model model) {
        // Hardcode userId=2 for testing since that's what's in your database
        Long testUserId = 2L;

        try {
            List<Rental> activeRentals = rentalService.getActiveRentals(testUserId);
            List<Rental> previousRentals = rentalService.getPreviousRentals(testUserId);

            // Add debug logging
            System.out.println("Active rentals found: " + (activeRentals != null ? activeRentals.size() : "null"));
            System.out
                    .println("Previous rentals found: " + (previousRentals != null ? previousRentals.size() : "null"));

            model.addAttribute("activeRentals", activeRentals);
            model.addAttribute("previousRentals", previousRentals);

            return "rental-history";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load rental history: " + e.getMessage());
            return "rental-history";
        }
    }

    @PostMapping("/{rentalId}/return")
    public ResponseEntity<?> returnRental(@PathVariable Long rentalId) {
        try {
            Rental rental = rentalService.returnRental(rentalId);
            return ResponseEntity.ok(rental);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        Long testUserId = 2L;

        try {
            List<Rental> cartItems = rentalService.getActiveRentals(testUserId);
            
            // Fetch game details for each rental
            cartItems.forEach(rental -> {
                Game game = gameService.getGameById(rental.getGameId());
                rental.setGame(game);  // Assuming you have a setGame method in Rental
            });

            model.addAttribute("cartItems", cartItems);

            BigDecimal totalPrice = cartItems.stream()
                    .map(Rental::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("totalPrice", totalPrice);

            return "cart";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load cart: " + e.getMessage());
            return "cart";
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception here
        return ResponseEntity.internalServerError().body("An unexpected error occurred");
    }
}