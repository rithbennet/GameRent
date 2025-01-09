package com.rental.gamerent.model;

import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming the `id` column is auto-incremented
    private Long id;
    private Long reviews; // Assuming the reviews are some kind of rating or identifier
    private String feedback; // Changed to lowercase for consistency
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false) // Foreign key to Game table
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to Users table
    private Users user;

    public Review() {
    }

    // Constructor
    public Review(Game game, Users user) {
        this.game = game;
        this.user = user;
    }
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}