package com.noaats.backend.dto.promo;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import com.noaats.backend.promo.PaymentMethod;

public record PromoRequestDto(
	@Schema(description = "Subtotal of items (excluding shipping). This will be recomputed from items.", example = "59000")
	@PositiveOrZero long subtotal,
	@ArraySchema(schema = @Schema(implementation = CartItemDto.class), minItems = 1)
	@Valid @NotNull @NotEmpty List<CartItemDto> items,
	@Schema(description = "Shipping fee", example = "3000")
	@PositiveOrZero long shippingFee,
	@Schema(description = "Payment method", example = "CARD")
	@NotNull PaymentMethod paymentMethod,
	@ArraySchema(schema = @Schema(implementation = PriceCouponDto.class), minItems = 0)
	@Valid @NotNull List<PriceCouponDto> priceCoupons,
	@ArraySchema(schema = @Schema(implementation = ShippingCouponDto.class), minItems = 0)
	@Valid @NotNull List<ShippingCouponDto> shippingCoupons
) {}
