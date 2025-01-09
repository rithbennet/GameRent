package com.rental.gamerent.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "platform", nullable = false)
    private String platform;

    @Column(name = "price_per_day", nullable = false)
    private Double pricePerDay;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "availability", nullable = false)
    private Boolean availability;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "requirement_memory")
    private String requirementMemory;

    @Column(name = "requirement_processor")
    private String requirementProcessor;

    @Column(name = "requirement_graphics")
    private String requirementGraphics;

    @Column(name = "requirement_storage")
    private String requirementStorage;

    @Column(name = "favorited_at")
    private LocalDateTime favoritedAt;
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirementMemory() {
        return requirementMemory;
    }

    public void setRequirementMemory(String requirementMemory) {
        this.requirementMemory = requirementMemory;
    }

    public String getRequirementProcessor() {
        return requirementProcessor;
    }

    public void setRequirementProcessor(String requirementProcessor) {
        this.requirementProcessor = requirementProcessor;
    }

    public String getRequirementGraphics() {
        return requirementGraphics;
    }

    public void setRequirementGraphics(String requirementGraphics) {
        this.requirementGraphics = requirementGraphics;
    }

    public String getRequirementStorage() {
        return requirementStorage;
    }

    public void setRequirementStorage(String requirementStorage) {
        this.requirementStorage = requirementStorage;
    }

    public LocalDateTime getFavoritedAt() {
        return favoritedAt;
    }

    public void setFavoritedAt(LocalDateTime favoritedAt) {
        this.favoritedAt = favoritedAt;
    }
}
