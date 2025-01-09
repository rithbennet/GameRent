package com.rental.gamerent.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.rental.gamerent.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Review;
import com.rental.gamerent.model.UserPrincipal;
import com.rental.gamerent.repo.GameRepo;
import com.rental.gamerent.repo.ReviewRepo;
import com.rental.gamerent.repo.UserRepo;
import com.rental.gamerent.service.GameService;

@Controller
public class GameController {


    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepo usersRepo;


    @Autowired
    private GameService gameService;

    @GetMapping("/games")
    public String listGames(Model model) {
        System.out.println("listGames method called");
        List<Game> games = gameService.getAllGames().stream().map(game -> {
            game.setReleaseDateFormatted(game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            return game;
        }).collect(Collectors.toList());
        System.out.println("Games in controller: " + games);
        model.addAttribute("games", games);
        return "GameCatalog/list";
    }
    

    @GetMapping("/games/{id}")
    public String gameDetails(@PathVariable Long id, Model model) {
        Game game = gameService.getGameById(id);
        game.setReleaseDateFormatted(game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        List<Review> reviews = reviewService.getReviewsByGameId(id);
        model.addAttribute("game", game);
        model.addAttribute("reviews", reviews);

        
        long currentUserId = getCurrentUserId();
        // Check if the user has already reviewed the game
        boolean userHasReviewed = reviews.stream().anyMatch(review -> review.getUser().getId() == currentUserId);
        model.addAttribute("userHasReviewed", userHasReviewed);
        return "GameCatalog/details";
    }

    @GetMapping("/api/games/{id}")
    @ResponseBody
    public ResponseEntity<Game> getGameDetails(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/games")
    public String addGame(@ModelAttribute Game game) {
        gameService.saveGame(game);
        return "redirect:/games";
    }

    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/games/{id}/rent")
    public String showRentalForm(@PathVariable("id") Long id, Model model) {
        Game selectedGame = gameService.getGameById(id);
        model.addAttribute("selectedGame", selectedGame);
        model.addAttribute("userId", 2L); // Hardcoded for now, replace with actual user ID later
        return "rental-form";
    }

     private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getId();
        }
        throw new IllegalStateException("User not authenticated");
    }
}