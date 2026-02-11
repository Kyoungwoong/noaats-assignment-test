package com.noaats.backend.promo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Price coupon type")
public enum PriceCouponType {
	@Schema(description = "Percent-based discount")
	PERCENT,
	@Schema(description = "Fixed-amount discount")
	FIXED
}
