package com.noaats.backend.dto.promo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

public record CartItemDto(
	@Schema(description = "Item name", example = "Sneakers")
	@NotBlank String name,
	@Schema(description = "Item price", example = "59000")
	@Positive long price,
	@Schema(description = "Quantity", example = "1")
	@Positive int quantity,
	@Schema(description = "Category", example = "SHOES")
	@NotBlank String category
) {}
