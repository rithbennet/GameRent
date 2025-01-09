package com.rental.gamerent.service;

import com.rental.gamerent.model.Game;
import com.rental.gamerent.model.Rental;
import com.rental.gamerent.model.RentalStatus;
import com.rental.gamerent.repo.RentalRepo;
import com.rental.gamerent.repo.GameRepo;
import com.rental.gamerent.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class RentalService {
    private final RentalRepo rentalRepository;
    private final GameRepo gameRepository;
    private final UserRepo userRepository;

    @Autowired
    public RentalService(RentalRepo rentalRepository, GameRepo gameRepository, UserRepo userRepository) {
        this.rentalRepository = rentalRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void updateExpiredRentals() {
        List<Rental> expiredRentals = rentalRepository.findByRentalStatusAndRentalEndDateBefore(
                RentalStatus.ACTIVE, LocalDate.now());

        for (Rental rental : expiredRentals) {
            rental.setRentalStatus(RentalStatus.RETURNED);
        }

        rentalRepository.saveAll(expiredRentals);
    }

    public Rental createRental(Long gameId, Long userId, LocalDate startDate, LocalDate endDate, RentalStatus status) {
        // Validate game exists and get game details
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));

        // Validate user exists
        if (!userRepository.existsById(userId.intValue())) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // Calculate rental duration and total price
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days < 1) {
            throw new IllegalArgumentException("Rental period must be at least 1 day");
        }

        BigDecimal pricePerDay = BigDecimal.valueOf(game.getPricePerDay());
        BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(days));

        // Create and save the rental
        Rental rental = new Rental();
        rental.setGameId(gameId);
        rental.setUserId(userId);
        rental.setRentalStartDate(startDate);
        rental.setRentalEndDate(endDate);
        rental.setRentalStatus(status); // Set the passed status
        rental.setTotalPrice(totalPrice);

        return rentalRepository.save(rental);
    }

    public void deleteRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found"));
        rentalRepository.delete(rental);
    }

    public List<Rental> getActiveRentals(Long userId) {
        LocalDate today = LocalDate.now();
        return rentalRepository.findByUserIdAndRentalStatusAndDateRange(userId, RentalStatus.ACTIVE, today);
    }

    public List<Rental> getCartRentals(Long userId) {
        return rentalRepository.findByUserIdAndRentalStatus(userId, RentalStatus.CART);
    }

    public List<Rental> getPreviousRentals(Long userId) {
        // Update expired rentals
        List<Rental> expiredRentals = rentalRepository.findByUserIdAndRentalStatusAndRentalEndDateBefore(
                userId, RentalStatus.ACTIVE, LocalDate.now());

        expiredRentals.forEach(rental -> rental.setRentalStatus(RentalStatus.RETURNED));
        rentalRepository.saveAll(expiredRentals);

        // Return all previous rentals
        return rentalRepository.findByUserIdAndRentalStatus(userId, RentalStatus.RETURNED);
    }

    @Transactional
    public void checkoutRentals(Long userId) {
        // Fetch rentals in the cart
        List<Rental> cartRentals = rentalRepository.findByUserIdAndRentalStatus(userId, RentalStatus.CART);

        if (cartRentals.isEmpty()) {
            throw new IllegalStateException("No items in cart to checkout");
        }

        // Change status to ACTIVE for these rentals
        for (Rental rental : cartRentals) {
            rental.setRentalStatus(RentalStatus.ACTIVE);
        }

        // Save updated rentals
        rentalRepository.saveAll(cartRentals);

        // Add logging for debugging
        System.out.println("Updated " + cartRentals.size() + " rentals from CART to ACTIVE for user " + userId);
    }

    @Transactional
    public Rental returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found: " + rentalId));

        if (rental.getRentalStatus() == RentalStatus.RETURNED) {
            throw new IllegalStateException("Rental is already returned");
        }

        rental.setRentalStatus(RentalStatus.RETURNED);
        return rentalRepository.save(rental);
    }

    // Additional helper method to check if a game is available for specific dates
    private boolean isGameAvailableForDates(Long gameId, LocalDate startDate, LocalDate endDate) {
        // Implementation would check for any overlapping active rentals
        // This is just a placeholder - you would need to implement the actual logic
        return true;
    }
}