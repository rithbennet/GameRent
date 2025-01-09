package com.rental.gamerent.service;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Review;
import com.rental.gamerent.model.Users;
import com.rental.gamerent.repo.GameRepo;
import com.rental.gamerent.repo.ReviewRepo;
import com.rental.gamerent.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private GameRepo gameRepo;

    @Autowired
    private UserRepo usersRepo;

    // Fetch reviews by game_id
    public List<Review> getReviewsByGameId(Long gameId) {
        return reviewRepo.findByGameId(gameId);
    }

    // Save a new review to the database
    public void saveReview(Long gameId, Long userId, Review review) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));
        Users user = usersRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        review.setGame(game);
        review.setUser(user);
        reviewRepo.save(review);
    }

    // Delete a review by ID
    public void deleteReviewById(Long id) {
        reviewRepo.deleteById(id);
    }
}