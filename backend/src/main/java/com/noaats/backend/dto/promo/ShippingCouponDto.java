package com.noaats.backend.dto.promo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.Schema;

public record ShippingCouponDto(
	@Schema(description = "Shipping discount amount", example = "3000")
	@NotNull @PositiveOrZero Long shippingDiscount,
	@Schema(description = "Shipping discount cap", example = "3000")
	@PositiveOrZero Long cap
) {}
