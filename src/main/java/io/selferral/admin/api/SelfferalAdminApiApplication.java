package io.selferral.admin.api;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import jakarta.annotation.PostConstruct;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SelfferalAdminApiApplication {
	
	@PostConstruct
	void startedTime() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SelfferalAdminApiApplication.class, args);
	}
}