package com.example.toledoBank;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ToledoBankApplication {

	@Bean
	public ModelMapper modelMapper(){ return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(ToledoBankApplication.class, args);
	}

}
