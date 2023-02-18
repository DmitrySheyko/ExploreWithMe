package ru.practicum.statservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of stat-server.
 *
 * @author DmitrySheyko
 */
@SpringBootApplication
public class StatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatServiceApplication.class, args);
	}
}