package com.noaats.backend.dto.promo;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PromoRequestDto(
	@PositiveOrZero long subtotal,
	@PositiveOrZero long shippingFee,
	@Valid @NotNull @NotEmpty List<PriceCouponDto> priceCoupons,
	@Valid @NotNull @NotEmpty List<ShippingCouponDto> shippingCoupons
) {}
