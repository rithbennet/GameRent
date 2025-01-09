package com.rental.gamerent.service;

import com.rental.gamerent.model.Review;
import com.rental.gamerent.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Fetch reviews by game_id
    public List<Review> getReviewsByGameId(Long gameId) {
        return reviewRepository.findByGameId(gameId);
    }

    // Save a new review to the database
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }
}