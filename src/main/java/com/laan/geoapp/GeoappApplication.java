package com.laan.geoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeoappApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoappApplication.class, args);
	}

}
