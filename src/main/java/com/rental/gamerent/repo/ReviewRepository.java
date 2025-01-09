package com.rental.gamerent.repo;

import com.rental.gamerent.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Custom query to find reviews by game_id
    @Query("SELECT r FROM Review r WHERE r.game_id = :gameId")
    List<Review> findByGameId(Long gameId);
}