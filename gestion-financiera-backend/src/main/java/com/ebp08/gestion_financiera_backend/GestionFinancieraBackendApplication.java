package com.ebp08.gestion_financiera_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Activa el "Motor" de tareas programadas 
public class GestionFinancieraBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionFinancieraBackendApplication.class, args);
	}

}
