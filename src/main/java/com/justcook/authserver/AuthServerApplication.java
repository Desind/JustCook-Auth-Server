package com.justcook.authserver;

import com.justcook.authserver.model.User.Status;
import com.justcook.authserver.model.User.User;
import com.justcook.authserver.model.User.UserType;
import com.justcook.authserver.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class AuthServerApplication {

	public static void main(String[] args){
		SpringApplication.run(AuthServerApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository repository){
		return args -> {
			User user = new User("Desind",
						"zaran1998@gmail.com",
						"testpass",
						LocalDateTime.now(),
						UserType.ADMIN,
						Status.CONFIRMED,
						List.of()
			);
			repository.insert(user);
		};
	}

}
