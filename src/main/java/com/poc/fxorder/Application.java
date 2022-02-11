package com.poc.fxorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class Application
 * 
 * It is the bootstrap class
 * 
 * @author PM
 *
 */
@SpringBootApplication
public class Application {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		log.debug("++++++++++++++++++++++++++++: FX-Trading capture application is being started!");
		SpringApplication.run(Application.class, args);
	
	}
}
