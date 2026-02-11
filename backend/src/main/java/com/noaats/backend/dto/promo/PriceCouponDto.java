package com.noaats.backend.dto.promo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.Schema;

import com.noaats.backend.promo.PriceCouponType;

@ValidPriceCoupon
public record PriceCouponDto(
	@Schema(description = "Coupon type", example = "PERCENT", allowableValues = {"PERCENT", "FIXED"})
	@NotNull PriceCouponType type,
	@Schema(description = "Percent discount rate (required when type=PERCENT)", example = "20")
	@PositiveOrZero Integer ratePercent,
	@Schema(description = "Fixed discount amount (required when type=FIXED)", example = "7000")
	@PositiveOrZero Long amount,
	@Schema(description = "Minimum spend for eligibility", example = "50000")
	@PositiveOrZero Long minSpend,
	@Schema(description = "Maximum discount cap", example = "8000")
	@PositiveOrZero Long cap
) {}
