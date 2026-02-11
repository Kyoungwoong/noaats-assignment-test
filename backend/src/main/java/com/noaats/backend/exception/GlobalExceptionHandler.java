package com.noaats.backend.exception;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.noaats.backend.api.ApiErrorResponse;
import com.noaats.backend.api.ErrorCode;
import com.noaats.backend.api.FieldErrorDetail;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		List<FieldErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
			.map(this::toFieldError)
			.collect(Collectors.toList());
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.VALIDATION_ERROR,
			"Invalid request",
			details,
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.status()).body(body);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
			HttpServletRequest request) {
		List<FieldErrorDetail> details = ex.getConstraintViolations().stream()
			.map(violation -> new FieldErrorDetail(violation.getPropertyPath().toString(), violation.getMessage()))
			.collect(Collectors.toList());
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.VALIDATION_ERROR,
			"Invalid request",
			details,
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.status()).body(body);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.BAD_REQUEST,
			ex.getMessage(),
			List.of(),
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.BAD_REQUEST.status()).body(body);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex,
			HttpServletRequest request) {
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.BAD_REQUEST,
			"Malformed JSON request",
			List.of(),
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.BAD_REQUEST.status()).body(body);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request) {
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.METHOD_NOT_ALLOWED,
			ex.getMessage(),
			List.of(),
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.METHOD_NOT_ALLOWED.status()).body(body);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ApiErrorResponse> handleMediaType(HttpMediaTypeNotSupportedException ex,
			HttpServletRequest request) {
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.UNSUPPORTED_MEDIA_TYPE,
			ex.getMessage(),
			List.of(),
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.UNSUPPORTED_MEDIA_TYPE.status()).body(body);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleNotFound(NoHandlerFoundException ex,
			HttpServletRequest request) {
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.NOT_FOUND,
			"Resource not found",
			List.of(),
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.NOT_FOUND.status()).body(body);
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex,
			HttpServletRequest request) {
		ApiErrorResponse body = ApiErrorResponse.from(
			ex.errorCode(),
			ex.getMessage(),
			List.of(),
			request.getRequestURI()
		);
		return ResponseEntity.status(ex.errorCode().status()).body(body);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex,
			HttpServletRequest request) {
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.FORBIDDEN,
			ErrorCode.FORBIDDEN.defaultMessage(),
			List.of(),
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.FORBIDDEN.status()).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
		log.error("Unhandled exception", ex);
		ApiErrorResponse body = ApiErrorResponse.from(
			ErrorCode.INTERNAL_ERROR,
			"Unexpected error",
			List.of(),
			request.getRequestURI()
		);
		return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.status()).body(body);
	}

	private FieldErrorDetail toFieldError(FieldError error) {
		return new FieldErrorDetail(error.getField(), error.getDefaultMessage());
	}
}
