package com.rental.gamerent.repo;
import com.rental.gamerent.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);


    @Override
    Optional<Users> findById(Long userId);
}