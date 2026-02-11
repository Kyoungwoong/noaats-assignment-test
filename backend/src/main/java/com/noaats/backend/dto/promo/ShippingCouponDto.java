package com.noaats.backend.dto.promo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ShippingCouponDto(
	@NotNull @PositiveOrZero Long shippingDiscount,
	@PositiveOrZero Long cap
) {}
