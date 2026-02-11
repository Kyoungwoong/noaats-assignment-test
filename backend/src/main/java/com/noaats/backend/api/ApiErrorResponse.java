package com.noaats.backend.api;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
	boolean success,
	String code,
	String message,
	List<FieldErrorDetail> details,
	String path,
	String timestamp
) {
	public static ApiErrorResponse from(ErrorCode code, String message, List<FieldErrorDetail> details, String path) {
		return new ApiErrorResponse(
			false,
			code.name(),
			message == null ? code.defaultMessage() : message,
			details,
			path,
			Instant.now().toString()
		);
	}
}
