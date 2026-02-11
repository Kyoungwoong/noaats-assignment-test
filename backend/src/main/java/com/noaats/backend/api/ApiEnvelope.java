package com.noaats.backend.api;

import java.time.Instant;
import java.util.Map;

public record ApiEnvelope<T>(
	boolean success,
	T data,
	Map<String, Object> meta
) {
	public static <T> ApiEnvelope<T> ok(T data) {
		return new ApiEnvelope<>(true, data, Map.of("timestamp", Instant.now().toString()));
	}
}
