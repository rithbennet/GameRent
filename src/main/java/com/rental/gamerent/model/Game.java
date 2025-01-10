package com.rental.gamerent.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "games") // Specify the table name here
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private String platform;

    @Column(name = "price_per_day")
    private Double pricePerDay;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String releaseDateFormatted;

    @Column(name = "requirement_memory")
    private String requirementMemory;

    @Column(name = "requirement_processor")
    private String requirementProcessor;

    @Column(name = "requirement_graphics")
    private String requirementGraphics;

    @Column(name = "requirement_storage")
    private String requirementStorage;

    @Column(name = "description")
    private String description;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDateFormatted() {
        return releaseDateFormatted;
    }

    public void setReleaseDateFormatted(String releaseDateFormatted) {
        this.releaseDateFormatted = releaseDateFormatted;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}