package com.rental.gamerent.service;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.repo.GameRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepo gameRepo;

    public List<Game> getAllGames() {
        List<Game> games = gameRepo.findAll();
        System.out.println("Fetched games: " + games);
        if (games.isEmpty()) {
            System.out.println("No games found");
        }
        return games;
    }

    public Game getGameById(Long id) {
        Optional<Game> game = gameRepo.findById(id);
        return game.orElse(null);
    }

    public void saveGame(Game game) {
        gameRepo.save(game);
    }

    public void deleteGame(Long id) {
        gameRepo.deleteById(id);
    }

    // Method to search games by title
    public List<Game> searchGamesByTitle(String title) {
        return gameRepo.findByTitleContainingIgnoreCase(title);
    }
}