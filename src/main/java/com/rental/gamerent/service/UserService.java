package com.rental.gamerent.service;

import com.rental.gamerent.model.Users;
import com.rental.gamerent.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private static final String DEFAULT_ROLE = "CUSTOMER";
    @Autowired
    private UserRepo userRepo;

    public void registerUser(String username, String password, String email) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username is already taken!");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(DEFAULT_ROLE);

        userRepo.save(user);


    }
}