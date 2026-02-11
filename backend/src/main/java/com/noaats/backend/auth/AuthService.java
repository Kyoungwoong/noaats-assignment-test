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
		if (userRepository.existsByUsername(username)) {
			throw new ApiException(ErrorCode.BAD_REQUEST, "Username already exists");
		}
		UserEntity user = new UserEntity(username, passwordEncoder.encode(password));
		UserEntity saved = userRepository.save(user);
		return jwtService.generateToken(saved);
	}

	public String login(String username, String password) {
		UserEntity user = userRepository.findByUsername(username)
			.orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials"));
		if (!passwordEncoder.matches(password, user.getPasswordHash())) {
			throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
		}
		return jwtService.generateToken(user);
	}
}
