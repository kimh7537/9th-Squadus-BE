package com.cotato.squadus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SquadusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SquadusApplication.class, args);
	}

}
