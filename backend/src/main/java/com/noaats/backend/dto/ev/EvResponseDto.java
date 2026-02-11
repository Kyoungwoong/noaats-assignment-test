package com.noaats.backend.dto.ev;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record EvResponseDto(
	@Schema(description = "Expected value (KRW)", example = "2500")
	long expectedValue,
	@Schema(description = "Expected discount rate", example = "0.15")
	double expectedDiscountRate,
	@ArraySchema(schema = @Schema(implementation = EvScenarioResultDto.class))
	List<EvScenarioResultDto> scenarios
) {}
