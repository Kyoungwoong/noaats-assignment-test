package com.noaats.backend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.noaats.backend.api.ApiEnvelope;
import com.noaats.backend.auth.AuthService;
import com.noaats.backend.dto.auth.AuthRequestDto;
import com.noaats.backend.dto.auth.AuthResponseDto;

@RestController
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/api/auth/register")
	@Operation(summary = "Register", description = "Registers a new user and returns a JWT.")
	public ApiEnvelope<AuthResponseDto> register(@Valid @RequestBody AuthRequestDto request) {
		String token = authService.register(request.username(), request.password());
		return ApiEnvelope.ok(new AuthResponseDto(token));
	}

	@PostMapping("/api/auth/login")
	@Operation(summary = "Login", description = "Logs in and returns a JWT.")
	public ApiEnvelope<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
		String token = authService.login(request.username(), request.password());
		return ApiEnvelope.ok(new AuthResponseDto(token));
	}
}
