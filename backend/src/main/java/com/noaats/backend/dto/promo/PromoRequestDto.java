package com.noaats.backend.dto.promo;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record PromoRequestDto(
	@Schema(description = "Subtotal of items (excluding shipping)", example = "59000")
	@PositiveOrZero long subtotal,
	@Schema(description = "Shipping fee", example = "3000")
	@PositiveOrZero long shippingFee,
	@ArraySchema(schema = @Schema(implementation = PriceCouponDto.class), minItems = 1)
	@Valid @NotNull @NotEmpty List<PriceCouponDto> priceCoupons,
	@ArraySchema(schema = @Schema(implementation = ShippingCouponDto.class), minItems = 1)
	@Valid @NotNull @NotEmpty List<ShippingCouponDto> shippingCoupons
) {}
