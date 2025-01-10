package com.rental.gamerent.repo;

import com.rental.gamerent.model.Rental;
import com.rental.gamerent.model.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepo extends JpaRepository<Rental, Long> {

    // Active rentals within the date range
    @Query("SELECT r FROM Rental r WHERE r.userId = :userId AND r.rentalStatus = :status AND r.rentalStartDate <= :today AND r.rentalEndDate >= :today")
    List<Rental> findByUserIdAndRentalStatusAndDateRange(
            @Param("userId") Long userId,
            @Param("status") RentalStatus status,
            @Param("today") LocalDate today
    );

    // Expired rentals
    List<Rental> findByRentalStatusAndRentalEndDateBefore(RentalStatus status, LocalDate date);

    // Previous rentals (manually updated to RETURNED)
    List<Rental> findByUserIdAndRentalStatus(Long userId, RentalStatus status);

    // Rentals ending before today
    List<Rental> findByUserIdAndRentalStatusAndRentalEndDateBefore(Long userId, RentalStatus status, LocalDate date);
}
