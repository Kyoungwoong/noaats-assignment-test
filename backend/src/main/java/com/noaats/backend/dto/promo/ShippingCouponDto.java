package com.noaats.backend.dto.promo;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import com.noaats.backend.promo.PaymentMethod;

public record ShippingCouponDto(
	@Schema(description = "Shipping discount amount", example = "3000")
	@NotNull @PositiveOrZero Long shippingDiscount,
	@Schema(description = "Shipping discount cap", example = "3000")
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
