package com.noaats.backend.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;

class JwtServiceTest {

	@Test
	void generates_and_parses_token() {
		String secret = "01234567890123456789012345678901";
		JwtService jwtService = new JwtService(secret, 60);
		UserEntity user = new UserEntity("user", "hashed");

		String token = jwtService.generateToken(user);

		assertNotNull(token);
		Claims claims = jwtService.parseClaims(token);
		assertEquals("user", claims.getSubject());
	}
}
