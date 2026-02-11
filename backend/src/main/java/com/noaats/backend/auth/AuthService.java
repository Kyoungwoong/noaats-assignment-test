package com.noaats.backend.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noaats.backend.api.ErrorCode;
import com.noaats.backend.exception.ApiException;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public String register(String username, String password) {
		String trimmedUsername = username.trim();
		String trimmedPassword = password.trim();
		return userRepository.findByUsername(trimmedUsername)
			.map(existing -> {
				if (passwordEncoder.matches(trimmedPassword, existing.getPasswordHash())) {
					return jwtService.generateToken(existing);
				}
				throw new ApiException(ErrorCode.CONFLICT, "Username already exists");
			})
			.orElseGet(() -> {
				UserEntity user = new UserEntity(trimmedUsername, passwordEncoder.encode(trimmedPassword));
				UserEntity saved = userRepository.save(user);
				return jwtService.generateToken(saved);
			});
	}

	public String login(String username, String password) {
		String trimmedUsername = username.trim();
		String trimmedPassword = password.trim();
		UserEntity user = userRepository.findByUsername(trimmedUsername)
			.orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials"));
		if (!passwordEncoder.matches(trimmedPassword, user.getPasswordHash())) {
			throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
		}
		return jwtService.generateToken(user);
	}
}
