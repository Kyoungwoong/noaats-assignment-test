package com.noaats.backend.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.noaats.backend.auth.AuthService;
import com.noaats.backend.dto.auth.AuthRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthControllerTest {

	@Test
	void returnsTokenOnRegister() {
		AuthService authService = Mockito.mock(AuthService.class);
		Mockito.when(authService.register("user", "pass")).thenReturn("token");
		AuthController controller = new AuthController(authService);

		var response = controller.register(new AuthRequestDto("user", "pass"));

		assertEquals(true, response.success());
		assertEquals("token", response.data().accessToken());
	}

	@Test
	void returnsTokenOnLogin() {
		AuthService authService = Mockito.mock(AuthService.class);
		Mockito.when(authService.login("user", "pass")).thenReturn("token");
		AuthController controller = new AuthController(authService);

		var response = controller.login(new AuthRequestDto("user", "pass"));

		assertEquals(true, response.success());
		assertEquals("token", response.data().accessToken());
	}
}
