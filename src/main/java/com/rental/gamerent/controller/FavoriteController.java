package com.rental.gamerent.controller;

import com.rental.gamerent.model.Favorite;
import com.rental.gamerent.model.UserPrincipal;
import com.rental.gamerent.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // Render the favorites page for the current user
    @GetMapping
    public String favorites(Model model) {
        Long userId = getCurrentUserId();
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
        model.addAttribute("favorites", favorites);
        model.addAttribute("newFavorite", new Favorite()); // Add empty Favorite object for the form
        return "favorites";
    }

    // Retrieve all favorites for a specific user
    @GetMapping("/user/{userId}")
    public String getUserFavorites(@PathVariable Long userId, Model model) {
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
        model.addAttribute("favorites", favorites);
        return "favorites";
    }

    // Handle form submission to add a new favorite
    @PostMapping("/add")
    public String addFavorite(@RequestParam Long gameId, @RequestParam Long userId) {
        favoriteService.addFavorite(gameId, userId); // Save the new favorite
        return "redirect:/favorites"; // Redirect to user's favorites page
    }

    // Delete a favorite by ID
    @PostMapping("/delete/{id}")
    public String deleteFavorite(@PathVariable Long id, @RequestParam Long userId) {
        favoriteService.deleteFavoriteById(id);
        return "redirect:/favorites"; // Redirect to user's favorites page
    }

    // Helper method to get the current user's ID from the session
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getId();
        }
        throw new IllegalStateException("User not authenticated");
    }
}