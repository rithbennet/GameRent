package com.rental.gamerent.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import com.rental.gamerent.model.Rental;
import com.rental.gamerent.model.RentalStatus;

@Controller
@RequestMapping("/rentals")
public class RentalController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/form")
    public String showRentalForm(Model model) {
        List<Game> games = entityManager.createQuery("from Game", Game.class).getResultList();
        model.addAttribute("games", games);
        return "rental-form";
    }

    @PostMapping("/create")
    @Transactional
    public String createRental(
            @RequestParam("gameId") Long gameId,
            @RequestParam("userId") Long userId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            Model model) {

        Game game = entityManager.find(Game.class, gameId);
        User user = entityManager.find(User.class, userId);

        BigDecimal pricePerDay = game.getPrice();
        long days = ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
        BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(days));

        Rental rental = new Rental();
        rental.setGame(game);
        rental.setUser(user);
        rental.setRentalStartDate(LocalDate.parse(startDate));
        rental.setRentalEndDate(LocalDate.parse(endDate));
        rental.setRentalStatus(RentalStatus.ACTIVE);
        rental.setTotalPrice(totalPrice);

        entityManager.persist(rental);
        model.addAttribute("rental", rental);
        return "rental-success";
    }

    @GetMapping("/cart")
    public String viewCart(@RequestParam("userId") Long userId, Model model) {
        List<Rental> activeRentals = entityManager
                .createQuery("from Rental where user.id = :userId and rentalStatus = :status", Rental.class)
                .setParameter("userId", userId)
                .setParameter("status", RentalStatus.ACTIVE)
                .getResultList();

        model.addAttribute("rentals", activeRentals);
        return "cart";
    }

    @GetMapping("/history")
    public String rentalHistory(@RequestParam("userId") Long userId, Model model) {
        List<Rental> activeRentals = entityManager
                .createQuery("from Rental where user.id = :userId and rentalStatus = :status", Rental.class)
                .setParameter("userId", userId)
                .setParameter("status", RentalStatus.ACTIVE)
                .getResultList();

        List<Rental> previousRentals = entityManager
                .createQuery("from Rental where user.id = :userId and rentalStatus = :status", Rental.class)
                .setParameter("userId", userId)
                .setParameter("status", RentalStatus.RETURNED)
                .getResultList();

        model.addAttribute("activeRentals", activeRentals);
        model.addAttribute("previousRentals", previousRentals);
        return "rentals-history";
    }
}
