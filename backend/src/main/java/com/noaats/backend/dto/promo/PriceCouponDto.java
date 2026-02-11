package com.noaats.backend.dto.promo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import com.noaats.backend.promo.PriceCouponType;

@ValidPriceCoupon
public record PriceCouponDto(
	@NotNull PriceCouponType type,
	@PositiveOrZero Integer ratePercent,
	@PositiveOrZero Long amount,
	@PositiveOrZero Long minSpend,
	@PositiveOrZero Long cap
) {}
