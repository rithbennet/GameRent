package com.rental.gamerent.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for the Favorite entity

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false) // Foreign key to Game table
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to Users table
    private Users user;

    // Default Constructor
    public Favorite() {
    }

    // Constructor
    public Favorite(Game game, Users user) {
        this.game = game;
        this.user = user;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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