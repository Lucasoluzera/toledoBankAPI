package com.example.toledoBank;

import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ToledoBankApplication {

	@Bean
	public ModelMapper modelMapper(){ return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(ToledoBankApplication.class, args);
	}

}
