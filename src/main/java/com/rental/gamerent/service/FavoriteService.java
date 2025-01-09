package com.rental.gamerent.service;

import com.rental.gamerent.model.Favorite;
import com.rental.gamerent.repo.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    public Optional<Favorite> getFavoriteById(Long id) {
        return favoriteRepository.findById(id);
    }

    public Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }
}
