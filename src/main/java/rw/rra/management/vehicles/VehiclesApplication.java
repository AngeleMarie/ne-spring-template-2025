package rw.rra.management.vehicles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import rw.rra.management.vehicles.users.UserService;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class VehiclesApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(VehiclesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userService.createAdminUserIfNotExists();
	}
}

