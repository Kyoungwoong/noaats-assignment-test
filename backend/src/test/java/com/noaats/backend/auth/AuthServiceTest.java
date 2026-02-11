package com.noaats.backend.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.noaats.backend.api.ErrorCode;
import com.noaats.backend.exception.ApiException;

class AuthServiceTest {

	@Test
	void register_creates_user_and_returns_token() {
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		JwtService jwtService = mock(JwtService.class);
		when(userRepository.existsByUsername("user")).thenReturn(false);
		when(encoder.encode("pass")).thenReturn("hashed");
		when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));
		when(jwtService.generateToken(any(UserEntity.class))).thenReturn("token");

		AuthService service = new AuthService(userRepository, encoder, jwtService);

		String token = service.register("user", "pass");

		assertEquals("token", token);
	}

	@Test
	void register_rejects_duplicate_username() {
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		JwtService jwtService = mock(JwtService.class);
		when(userRepository.existsByUsername("user")).thenReturn(true);

		AuthService service = new AuthService(userRepository, encoder, jwtService);

		ApiException ex = assertThrows(ApiException.class, () -> service.register("user", "pass"));
		assertEquals(ErrorCode.BAD_REQUEST, ex.errorCode());
	}

	@Test
	void login_returns_token_when_password_matches() {
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		JwtService jwtService = mock(JwtService.class);
		UserEntity user = new UserEntity("user", "hashed");
		when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
		when(encoder.matches("pass", "hashed")).thenReturn(true);
		when(jwtService.generateToken(user)).thenReturn("token");

		AuthService service = new AuthService(userRepository, encoder, jwtService);

		String token = service.login("user", "pass");

		assertEquals("token", token);
	}

	@Test
	void login_rejects_invalid_password() {
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		JwtService jwtService = mock(JwtService.class);
		UserEntity user = new UserEntity("user", "hashed");
		when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
		when(encoder.matches("pass", "hashed")).thenReturn(false);

		AuthService service = new AuthService(userRepository, encoder, jwtService);

		ApiException ex = assertThrows(ApiException.class, () -> service.login("user", "pass"));
		assertEquals(ErrorCode.UNAUTHORIZED, ex.errorCode());
	}

	@Test
	void login_rejects_missing_user() {
		UserRepository userRepository = mock(UserRepository.class);
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		JwtService jwtService = mock(JwtService.class);
		when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

		AuthService service = new AuthService(userRepository, encoder, jwtService);

		ApiException ex = assertThrows(ApiException.class, () -> service.login("user", "pass"));
		assertEquals(ErrorCode.UNAUTHORIZED, ex.errorCode());
	}
}
