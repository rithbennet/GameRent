package com.rental.gamerent.controller;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors; // Add this import statement

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/games")
    public String listGames(Model model) {
        System.out.println("listGames method called");
        List<Game> games = gameService.getAllGames().stream().map(game -> {
            game.setReleaseDateFormatted(game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            return game;
        }).collect(Collectors.toList());
        System.out.println("Games in controller: " + games);
        model.addAttribute("games", games);
        return "GameCatalog/list";
    }

    @GetMapping("/games/{id}")
    public String gameDetails(@PathVariable Long id, Model model) {
        Game game = gameService.getGameById(id);
        game.setReleaseDateFormatted(game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("game", game);
        return "GameCatalog/details";
    }
}