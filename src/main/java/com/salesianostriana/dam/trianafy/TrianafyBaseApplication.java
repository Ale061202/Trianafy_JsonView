package com.salesianostriana.dam.trianafy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info =
	@Info(description = "Primer proyecto de una API REST con Swagger",
			version = "1.0",
			title = "Trianafy"
	)
)
public class TrianafyBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrianafyBaseApplication.class, args);
	}

}
