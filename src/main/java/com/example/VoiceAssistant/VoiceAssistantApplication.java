package com.example.VoiceAssistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VoiceAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoiceAssistantApplication.class, args);
	}
}
