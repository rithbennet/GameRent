package com.rental.gamerent.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming the `id` column is auto-incremented
    private Long id;

    private Long reviews; // Assuming the reviews are some kind of rating or identifier
    private String feedback; // Changed to lowercase for consistency
    private Long game_id; // Updated to follow Java naming conventions

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviews() {
        return reviews;
    }

    public void setReviews(Long reviews) {
        this.reviews = reviews;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Long getGameId() {
        return game_id;
    }

    public void setGameId(Long gameId) {
        this.game_id = gameId;
    }
}