package com.noaats.backend.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "Promo Value Calculator API",
		description = "Promo calculation API with coupon policies and Top3 recommendations.",
		version = "v1",
		contact = @Contact(name = "NoahATS", email = "no-reply@noaats.local")
	)
)
public class OpenApiConfig {}
