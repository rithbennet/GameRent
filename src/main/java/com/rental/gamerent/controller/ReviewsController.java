package com.rental.gamerent.controller;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Review;
import com.rental.gamerent.service.GameService;
import com.rental.gamerent.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReviewsController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    public String reviews(@RequestParam(value = "title", required = false) String title, Model model) {
        model.addAttribute("title", "Game Reviews");

        // Search games or return all games if no query
        List<Game> games = (title != null && !title.isEmpty())
                ? gameService.searchGamesByTitle(title)
                : gameService.getAllGames();

        model.addAttribute("message",
                title != null && !title.isEmpty() ? "Search results for: " + title : "Showing all games.");

        // Map games to their reviews
        Map<Game, List<Review>> gameReviews = new HashMap<>();
        for (Game game : games) {
            List<Review> reviews = reviewService.getReviewsByGameId(game.getId());
            gameReviews.put(game, reviews);
        }

        model.addAttribute("gameReviews", gameReviews);
        return "reviews";
    }

    @PostMapping("/reviews/add")
    public String addReview(@RequestParam("gameId") Long gameId,
                            @RequestParam("reviews") Long reviews,
                            @RequestParam("feedback") String feedback) {

        // Fetch the game by ID
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            return "redirect:/reviews?error=GameNotFound";
        }

        // Create and save a new review
        Review newReview = new Review();
        newReview.setGameId(gameId);
        newReview.setReviews(reviews); // Set the rating
        newReview.setFeedback(feedback); // Set the feedback
        reviewService.saveReview(newReview);

        // Redirect back to the reviews page
        return "redirect:/reviews?title=" + game.getTitle();
    }
}