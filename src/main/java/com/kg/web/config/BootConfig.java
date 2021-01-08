package com.kg.web.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.kg.web.*" })  
public class BootConfig {

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(BootConfig.class, args);
	}

}
