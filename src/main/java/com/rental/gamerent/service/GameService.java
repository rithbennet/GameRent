package com.rental.gamerent.service;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.repo.GameRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}