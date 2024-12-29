package com.statusup.statusup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.statusup.statusup")
public class StatusupApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatusupApplication.class, args);
	}

}
