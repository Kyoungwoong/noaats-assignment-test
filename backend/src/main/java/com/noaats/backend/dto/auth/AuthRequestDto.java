package com.noaats.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
	@NotBlank String username,
	@NotBlank String password
) {}
