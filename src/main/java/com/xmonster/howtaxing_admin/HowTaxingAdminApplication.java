package com.xmonster.howtaxing_admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HowTaxingAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(HowTaxingAdminApplication.class, args);
	}
}
