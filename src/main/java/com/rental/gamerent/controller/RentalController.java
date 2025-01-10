package com.rental.gamerent.controller;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Rental;
import com.rental.gamerent.model.RentalStatus;
import com.rental.gamerent.model.UserPrincipal;
import com.rental.gamerent.service.GameService;
import com.rental.gamerent.service.RentalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public RentalController(RentalService rentalService, GameService gameService) {
        this.rentalService = rentalService;
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public String createRental(
            @RequestParam("gameId") Long gameId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        try {
            Long userId = getCurrentUserId();
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
            Long userId = getCurrentUserId();
            List<Rental> activeRentals = rentalService.getActiveRentals(userId);
            List<Rental> previousRentals = rentalService.getPreviousRentals(userId);

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
        try {
            Long userId = getCurrentUserId();
            List<Rental> activeRentals = rentalService.getActiveRentals(userId);
            List<Rental> previousRentals = rentalService.getPreviousRentals(userId);

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
        try {
            Long userId = getCurrentUserId();
            List<Rental> cartItems = rentalService.getCartRentals(userId); // Fetch rentals with CART status
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
        try {
            Long userId = getCurrentUserId();
            rentalService.checkoutRentals(userId);
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

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getId();
        }
        throw new IllegalStateException("User not authenticated");
    }
}