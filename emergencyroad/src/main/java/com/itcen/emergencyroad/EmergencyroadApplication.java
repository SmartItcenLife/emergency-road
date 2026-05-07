package com.itcen.emergencyroad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EmergencyroadApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmergencyroadApplication.class, args);
	}

}
