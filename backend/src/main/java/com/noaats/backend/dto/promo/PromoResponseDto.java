package com.noaats.backend.dto.promo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record PromoResponseDto(
	@ArraySchema(schema = @Schema(implementation = PromoCombinationResultDto.class), minItems = 0)
	List<PromoCombinationResultDto> top3
) {}
