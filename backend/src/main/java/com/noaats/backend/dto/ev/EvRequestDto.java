package com.noaats.backend.dto.ev;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record EvRequestDto(
	@Schema(description = "Base amount for percent rewards", example = "59000")
	@PositiveOrZero long baseAmount,
	@ArraySchema(schema = @Schema(implementation = EvScenarioDto.class), minItems = 1)
	@Valid @NotNull @NotEmpty List<EvScenarioDto> scenarios
) {}
