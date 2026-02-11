package com.noaats.backend.api;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiErrorResponse")
public final class ApiErrorResponseSchema {
	@Schema(description = "Whether the request was successful", example = "false")
	public boolean success;

	@Schema(description = "Error code", example = "VALIDATION_ERROR")
	public String code;

	@Schema(description = "Error message", example = "Invalid request")
	public String message;

	@ArraySchema(schema = @Schema(implementation = FieldErrorDetail.class))
	public FieldErrorDetail[] details;

	@Schema(description = "Request path", example = "/api/promo/calculate")
	public String path;

	@Schema(description = "Timestamp (ISO-8601)", example = "2026-02-11T12:34:56Z")
	public String timestamp;
}
