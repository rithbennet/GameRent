package com.rental.gamerent.controller;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Review;
import com.rental.gamerent.model.UserPrincipal;
import com.rental.gamerent.service.GameService;
import com.rental.gamerent.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("reviews/add")
    public String addReview(@RequestParam Long gameId, @RequestParam Long reviews,
            @RequestParam String feedback) {
        Long userId = getCurrentUserId();
        Review review = new Review();
        review.setReviews(reviews);
        review.setFeedback(feedback);
        reviewService.saveReview(gameId, userId, review);
        return "redirect:/games/" + gameId;
    }

     @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id, @RequestParam("gameId") Long gameId) {
        reviewService.deleteReviewById(id);
        return "redirect:/games/" + gameId;
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