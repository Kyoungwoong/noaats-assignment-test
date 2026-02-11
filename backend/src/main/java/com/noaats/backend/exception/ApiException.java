package com.noaats.backend.exception;

import com.noaats.backend.api.ErrorCode;

public class ApiException extends RuntimeException {
	private final ErrorCode errorCode;

	public ApiException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ApiException(ErrorCode errorCode) {
		this(errorCode, errorCode.defaultMessage());
	}

	public ErrorCode errorCode() {
		return errorCode;
	}
}
