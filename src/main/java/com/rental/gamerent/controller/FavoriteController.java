package com.rental.gamerent.controller;

import com.rental.gamerent.model.Favorite;
import com.rental.gamerent.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // Render the favorites page
    @GetMapping("/page")
    public String getFavoritesPage(Model model) {
        List<Favorite> favorites = favoriteService.getAllFavorites();
        model.addAttribute("favorites", favorites);
        model.addAttribute("newFavorite", new Favorite()); // Add empty Favorite object for the form
        return "favorites";
    }

    // Retrieve all favorites as JSON
    @GetMapping
    @ResponseBody
    public List<Favorite> getAllFavorites() {
        return favoriteService.getAllFavorites();
    }

    // Retrieve a specific favorite by ID as JSON
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Favorite> getFavoriteById(@PathVariable Long id) {
        Optional<Favorite> favorite = favoriteService.getFavoriteById(id);
        return favorite.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Handle form submission to add a new favorite
    @PostMapping("/add")
    public String addFavorite(@ModelAttribute Favorite favorite) {
        favoriteService.saveFavorite(favorite); // Save the new favorite
        return "redirect:/favorites/page"; // Redirect to favorites page
    }

    // Delete a favorite by ID
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        if (favoriteService.getFavoriteById(id).isPresent()) {
            favoriteService.deleteFavoriteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}