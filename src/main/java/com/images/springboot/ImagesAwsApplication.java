package com.images.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ImagesAwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagesAwsApplication.class, args);
	}

}
