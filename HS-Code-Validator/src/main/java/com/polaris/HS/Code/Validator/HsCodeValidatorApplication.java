package com.polaris.HS.Code.Validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class HsCodeValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(HsCodeValidatorApplication.class, args);
	}

}
