package com.noaats.backend.dto.promo;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PromoRequestDto(
	@PositiveOrZero long subtotal,
	@PositiveOrZero long shippingFee,
	@Valid @NotNull List<PriceCouponDto> priceCoupons,
	@Valid @NotNull List<ShippingCouponDto> shippingCoupons
) {}
