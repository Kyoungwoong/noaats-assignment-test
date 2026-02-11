package com.noaats.backend.controller;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.noaats.backend.api.ApiEnvelopeSchema;
import com.noaats.backend.api.ApiEnvelope;

@RestController
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

	@GetMapping("/api/health")
	@Operation(summary = "Health check", description = "Returns API status and server time.")
	@ApiResponse(
		responseCode = "200",
		description = "Health status",
		content = @Content(schema = @Schema(implementation = ApiEnvelopeSchema.class))
	)
	public ApiEnvelope<HealthResponse> health() {
		return ApiEnvelope.ok(new HealthResponse("ok", Instant.now()));
	}

	public record HealthResponse(String status, Instant time) {}
}
