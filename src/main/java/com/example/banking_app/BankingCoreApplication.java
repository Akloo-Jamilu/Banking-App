package com.example.banking_app;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Banking Application",
				description = "App REST APIs",
				version = "v1.00",
				contact = @Contact(
						name = "Akloo",
						email = "contactAkloo",
						url = "Null"
				),
				license = @License(
						name = "Banking Application",
						url = "Null"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Banking APIs",
				url = "Null"
		)
)
public class BankingCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingCoreApplication.class, args);
	}

}
