package com.rental.gamerent.controller;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/games")
    public String listGames(Model model) {
        System.out.println("listGames method called");
        List<Game> games = gameService.getAllGames();
        System.out.println("Games in controller: " + games);
        model.addAttribute("games", games);
        return "GameCatalog/list";
    }
}