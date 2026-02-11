package com.noaats.backend.dto.ev;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.Schema;

import com.noaats.backend.ev.EvRewardType;

public record EvScenarioDto(
	@Schema(description = "Scenario label", example = "당첨 5%")
	@NotBlank String label,
	@Schema(description = "Probability (0~1)", example = "0.05")
	@Min(0) @Max(1) double probability,
	@Schema(description = "Reward type", example = "POINT")
	@NotNull EvRewardType rewardType,
	@Schema(description = "Reward value", example = "1000")
	@PositiveOrZero long rewardValue
) {}
