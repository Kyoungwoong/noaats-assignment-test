package com.noaats.backend.auth;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	private final JwtService jwtService;

	public JwtAuthFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				Claims claims = jwtService.parseClaims(token);
				Long userId = claims.get("uid", Long.class);
				String username = claims.getSubject();
				if (userId != null && username != null) {
					UserPrincipal principal = new UserPrincipal(userId, username);
					UsernamePasswordAuthenticationToken auth =
						new UsernamePasswordAuthenticationToken(principal, null, null);
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (Exception ignored) {
				// Invalid token -> allow entry point to handle if protected
			}
		}
		filterChain.doFilter(request, response);
	}
}
