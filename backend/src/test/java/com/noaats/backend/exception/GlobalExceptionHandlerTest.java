package com.noaats.backend.exception;

import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.noaats.backend.api.ErrorCode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

	@Test
	void handlesValidationErrors() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
		bindingResult.addError(new FieldError("request", "field", "required"));
		MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/test");

		var response = handler.handleValidation(ex, request);

		assertEquals(ErrorCode.VALIDATION_ERROR.status(), response.getStatusCode());
		assertEquals(ErrorCode.VALIDATION_ERROR.name(), response.getBody().code());
		assertEquals(1, response.getBody().details().size());
	}

	@Test
	void handlesConstraintViolation() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
		Path path = Mockito.mock(Path.class);
		Mockito.when(path.toString()).thenReturn("field");
		Mockito.when(violation.getPropertyPath()).thenReturn(path);
		Mockito.when(violation.getMessage()).thenReturn("invalid");
		ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");

		var response = handler.handleConstraintViolation(ex, request);

		assertEquals(ErrorCode.VALIDATION_ERROR.status(), response.getStatusCode());
		assertEquals(1, response.getBody().details().size());
	}

	@Test
	void handlesIllegalArgument() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");

		var response = handler.handleIllegalArgument(new IllegalArgumentException("bad"), request);

		assertEquals(ErrorCode.BAD_REQUEST.status(), response.getStatusCode());
		assertEquals("bad", response.getBody().message());
	}

	@Test
	void handlesNotReadable() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/test");

		var response = handler.handleNotReadable(new HttpMessageNotReadableException("bad", (HttpInputMessage) null), request);

		assertEquals(ErrorCode.BAD_REQUEST.status(), response.getStatusCode());
		assertEquals("Malformed JSON request", response.getBody().message());
	}

	@Test
	void handlesApiException() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");
		ApiException ex = new ApiException(ErrorCode.NOT_FOUND, "missing");

		var response = handler.handleApiException(ex, request);

		assertEquals(ErrorCode.NOT_FOUND.status(), response.getStatusCode());
		assertEquals("missing", response.getBody().message());
	}

	@Test
	void handlesAccessDenied() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");

		var response = handler.handleAccessDenied(new AccessDeniedException("denied"), request);

		assertEquals(ErrorCode.FORBIDDEN.status(), response.getStatusCode());
		assertEquals(ErrorCode.FORBIDDEN.defaultMessage(), response.getBody().message());
	}

	@Test
	void handlesUnexpected() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test");

		var response = handler.handleUnexpected(new RuntimeException("boom"), request);

		assertEquals(ErrorCode.INTERNAL_ERROR.status(), response.getStatusCode());
		assertEquals("Unexpected error", response.getBody().message());
	}

	@Test
	void handlesMethodNotAllowed() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/test");
		HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");

		var response = handler.handleMethodNotAllowed(ex, request);

		assertEquals(ErrorCode.METHOD_NOT_ALLOWED.status(), response.getStatusCode());
	}

	@Test
	void handlesMediaTypeNotSupported() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/test");
		HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("application/xml");

		var response = handler.handleMediaType(ex, request);

		assertEquals(ErrorCode.UNSUPPORTED_MEDIA_TYPE.status(), response.getStatusCode());
	}

	@Test
	void handlesNotFound() throws Exception {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/missing");
		NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/api/missing", null);

		var response = handler.handleNotFound(ex, request);

		assertEquals(ErrorCode.NOT_FOUND.status(), response.getStatusCode());
	}
}
