package com.rental.gamerent.repo;

import com.rental.gamerent.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepo extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
}
