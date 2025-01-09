package com.rental.gamerent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GamerentApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamerentApplication.class, args);
	}

}
