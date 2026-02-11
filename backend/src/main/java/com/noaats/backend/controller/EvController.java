package com.noaats.backend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.noaats.backend.api.ApiEnvelope;
import com.noaats.backend.api.ApiEnvelopeSchema;
import com.noaats.backend.api.ApiErrorResponseSchema;
import com.noaats.backend.dto.ev.EvRequestDto;
import com.noaats.backend.dto.ev.EvResponseDto;
import com.noaats.backend.ev.EvCalculator;

@RestController
@Tag(name = "EV", description = "Expected value promo endpoints")
public class EvController {

	@PostMapping("/api/ev/calculate")
	@Operation(summary = "Calculate EV", description = "Calculates expected value for probabilistic rewards.")
	@ApiResponse(
		responseCode = "200",
		description = "EV calculated",
		content = @Content(schema = @Schema(implementation = ApiEnvelopeSchema.class))
	)
	@ApiResponse(
		responseCode = "400",
		description = "Validation error",
		content = @Content(schema = @Schema(implementation = ApiErrorResponseSchema.class))
	)
	public ApiEnvelope<EvResponseDto> calculate(@Valid @RequestBody EvRequestDto request) {
		EvCalculator.EvResult result = EvCalculator.calculate(request.baseAmount(), request.scenarios());
		EvResponseDto response = new EvResponseDto(
			result.expectedValue(),
			result.expectedDiscountRate(),
			result.scenarios()
		);
		return ApiEnvelope.ok(response);
	}
}
