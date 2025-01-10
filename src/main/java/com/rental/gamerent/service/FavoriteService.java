package com.rental.gamerent.service;

import com.rental.gamerent.model.Favorite;
import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Users;
import com.rental.gamerent.repo.FavoriteRepo;
import com.rental.gamerent.repo.GameRepo;
import com.rental.gamerent.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private final FavoriteRepo favoriteRepository;
    private final GameRepo gameRepository; // Assume this is defined
    private final UserRepo userRepository; // Assume this is defined

    public FavoriteService(FavoriteRepo favoriteRepository,
                           GameRepo gameRepository,
                           UserRepo userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Favorite addFavorite(Long gameId, Long userId) {
        // Fetch the game and user from the database
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new favorite
        Favorite favorite = new Favorite(game, user);

        // Save to the database
        return favoriteRepository.save(favorite);
    }

    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }

    public Optional<Favorite> getFavoriteById(Long id) {
        return favoriteRepository.findById(id);
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
}