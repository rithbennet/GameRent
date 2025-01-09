package com.rental.gamerent.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.service.GameService;

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

    @GetMapping("/api/games/{id}")
    @ResponseBody
    public ResponseEntity<Game> getGameDetails(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/games")
    public String addGame(@ModelAttribute Game game) {
        gameService.saveGame(game);
        return "redirect:/games";
    }

    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}