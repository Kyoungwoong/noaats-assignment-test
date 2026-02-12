package com.noaats.backend.auth;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtAuthFilterTest {

	@AfterEach
	void clearContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void setsAuthenticationWhenTokenIsValid() throws ServletException, IOException {
		JwtService jwtService = Mockito.mock(JwtService.class);
		Claims claims = Mockito.mock(Claims.class);
		Mockito.when(claims.getSubject()).thenReturn("user");
		Mockito.when(claims.get("uid", Long.class)).thenReturn(1L);
		Mockito.when(jwtService.parseClaims("token")).thenReturn(claims);
		JwtAuthFilter filter = new JwtAuthFilter(jwtService);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = Mockito.mock(FilterChain.class);

		filter.doFilter(request, response, chain);

		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
		assertEquals("user", ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).username());
	}

	@Test
	void skipsAuthenticationWhenHeaderMissing() throws ServletException, IOException {
		JwtService jwtService = Mockito.mock(JwtService.class);
		JwtAuthFilter filter = new JwtAuthFilter(jwtService);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = Mockito.mock(FilterChain.class);

		filter.doFilter(request, response, chain);

		assertEquals(null, SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	void ignoresInvalidToken() throws ServletException, IOException {
		JwtService jwtService = Mockito.mock(JwtService.class);
		Mockito.when(jwtService.parseClaims("bad")).thenThrow(new RuntimeException("invalid"));
		JwtAuthFilter filter = new JwtAuthFilter(jwtService);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer bad");
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = Mockito.mock(FilterChain.class);

		filter.doFilter(request, response, chain);

		assertEquals(null, SecurityContextHolder.getContext().getAuthentication());
	}
}
