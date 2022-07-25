package com.parallelhash;

import com.parallelhash.service.UrlHashService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ParallelHashApplication {
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ParallelHashApplication.class, args);
		UrlHashService urlHashService = applicationContext.getBean(UrlHashService.class);
		urlHashService.generateUrlHashes();
	}
}