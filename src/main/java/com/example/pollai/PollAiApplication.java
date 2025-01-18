package com.example.pollai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PollAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollAiApplication.class, args);
	}

}
