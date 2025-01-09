package com.rental.gamerent.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Rental;
import com.rental.gamerent.model.RentalStatus;
import com.rental.gamerent.service.GameService;
import com.rental.gamerent.service.RentalService;

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
            Rental rental = rentalService.createRental(gameId, userId, startDate, endDate, RentalStatus.CART);
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

    @PostMapping("/{rentalId}/remove")
    public String removeCartItem(@PathVariable Long rentalId, Model model) {
        try {
            rentalService.deleteRental(rentalId);
            return "redirect:/rentals/cart"; // Redirect to the cart page after removal
        } catch (Exception e) {
            model.addAttribute("error", "Failed to remove item: " + e.getMessage());
            return "cart"; // Return the cart view with an error message
        }
    }

    @GetMapping("/active")
    public String getActiveRentals(Model model) {
        try {
            Long testUserId = 2L;
            List<Rental> activeRentals = rentalService.getActiveRentals(testUserId);
            List<Rental> previousRentals = rentalService.getPreviousRentals(testUserId);

            // Load game data for each rental
            activeRentals.forEach(rental -> {
                Game game = gameService.getGameById(rental.getGameId());
                rental.setGame(game);
            });

            previousRentals.forEach(rental -> {
                Game game = gameService.getGameById(rental.getGameId());
                rental.setGame(game);
            });

            model.addAttribute("activeRentals", activeRentals);
            model.addAttribute("previousRentals", previousRentals);

            return "rental-history";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load rentals: " + e.getMessage());
            return "rental-history";
        }
    }

    @GetMapping("/history")
    public String getRentalHistory(Model model) {
        Long testUserId = 2L;

        try {
            List<Rental> activeRentals = rentalService.getActiveRentals(testUserId);
            List<Rental> previousRentals = rentalService.getPreviousRentals(testUserId);

            // Add debug logging
            System.out.println("Loading game data for active rentals...");
            activeRentals.forEach(rental -> {
                Game game = gameService.getGameById(rental.getGameId());
                System.out.println("Found game for rental " + rental.getRentalId() + ": " +
                        (game != null ? game.getTitle() : "null"));
                rental.setGame(game);
            });

            System.out.println("Loading game data for previous rentals...");
            previousRentals.forEach(rental -> {
                Game game = gameService.getGameById(rental.getGameId());
                System.out.println("Found game for rental " + rental.getRentalId() + ": " +
                        (game != null ? game.getTitle() : "null"));
                rental.setGame(game);
            });

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
        Long testUserId = 2L; // Replace with actual user authentication

        try {
            List<Rental> cartItems = rentalService.getCartRentals(testUserId); // Fetch rentals with CART status
            cartItems.forEach(rental -> {
                Game game = gameService.getGameById(rental.getGameId());
                rental.setGame(game);
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

    @PostMapping("/checkout")
    public String checkoutRentals(Model model) {
        Long testUserId = 2L;
        try {
            rentalService.checkoutRentals(testUserId);
            model.addAttribute("message", "You have checked out successfully!");
            return "checkout-success";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to process checkout: " + e.getMessage());
            return "cart";
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception here
        return ResponseEntity.internalServerError().body("An unexpected error occurred");
    }
}