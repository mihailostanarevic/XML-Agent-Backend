package com.rentacar.agentbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgentBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgentBackendApplication.class, args);
	}

}
