package com.noaats.backend.dto.promo;

import io.swagger.v3.oas.annotations.media.Schema;

public record PriceDiscountResultDto(
	@Schema(description = "Whether price coupon was applied", example = "true")
	boolean applied,
	@Schema(description = "Discount amount from price coupon", example = "8000")
	long discount,
	@Schema(description = "Final amount after price coupon and shipping fee", example = "54000")
	long finalAmount,
	@Schema(description = "Reason code when not applied", example = "MIN_SPEND_NOT_MET")
	String reason,
	@Schema(description = "Shortfall amount to meet minSpend", example = "2000")
	long shortfallAmount
) {}
