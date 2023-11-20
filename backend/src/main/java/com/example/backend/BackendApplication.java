package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

//		String url = "";
//
//		WebClient.Builder builder = WebClient.builder();
//
//		String pageComponents = builder.build()
//				.get()
//				.uri(url)
//				.retrieve()
//				.bodyToMono(String.class)
//				.block();

	}

}
