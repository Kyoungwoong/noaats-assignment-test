package com.noaats.backend.dto.promo;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import com.noaats.backend.promo.PaymentMethod;
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
	@PositiveOrZero Long cap,
	@ArraySchema(schema = @Schema(description = "Excluded categories", example = "SHOES"))
	List<String> excludedCategories,
	@ArraySchema(schema = @Schema(implementation = PaymentMethod.class))
	List<PaymentMethod> allowedPaymentMethods,
	@Schema(description = "Valid from (ISO-8601)", example = "2026-02-01T00:00:00Z")
	Instant validFrom,
	@Schema(description = "Valid to (ISO-8601)", example = "2026-02-28T23:59:59Z")
	Instant validTo
) {}
