package com.mainsoft.tareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.transporte.logistica.repository")
public class GestionTransporteApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionTransporteApplication.class, args);
	}

}
