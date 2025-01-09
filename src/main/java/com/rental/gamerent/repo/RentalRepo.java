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
    @Query("SELECT r FROM Rental r WHERE r.userId = :userId AND r.rentalStatus = :status")
    List<Rental> findByUserIdAndRentalStatus(
            @Param("userId") Long userId,
            @Param("status") RentalStatus status
    );
     List<Rental> findByRentalStatusAndRentalEndDateBefore(RentalStatus rentalStatus, LocalDate date);
     List<Rental> findByUserIdAndRentalStatusAndRentalEndDateBefore(Long userId, RentalStatus rentalStatus, LocalDate date);

}
