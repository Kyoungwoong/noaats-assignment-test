package com.noaats.backend.dto.promo;

import io.swagger.v3.oas.annotations.media.Schema;

public record ShippingDiscountResultDto(
	@Schema(description = "Whether shipping coupon was applied", example = "true")
	boolean applied,
	@Schema(description = "Discount amount from shipping coupon", example = "3000")
	long discount,
	@Schema(description = "Remaining shipping fee after discount", example = "0")
	long remainingShippingFee,
	@Schema(description = "Reason code when not applied")
	String reason
) {}
