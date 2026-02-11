package com.noaats.backend.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiEnvelope")
public final class ApiEnvelopeSchema<T> {
	@Schema(description = "Whether the request was successful", example = "true")
	public boolean success;

	@Schema(description = "Response payload")
	public T data;
}
