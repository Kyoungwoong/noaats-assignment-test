package com.noaats.backend.dto.ev;

import io.swagger.v3.oas.annotations.media.Schema;

public record EvScenarioResultDto(
	@Schema(description = "Scenario label", example = "당첨 5%")
	String label,
	@Schema(description = "Probability", example = "0.05")
	double probability,
	@Schema(description = "Normalized reward (KRW)", example = "1000")
	long normalizedReward,
	@Schema(description = "Expected value for this scenario", example = "50")
	long expectedValue
) {}
