package com.noaats.backend.dto.promo;

public record PromoCombinationResultDto(
	PriceCouponDto priceCoupon,
	ShippingCouponDto shippingCoupon,
	PriceDiscountResultDto priceResult,
	ShippingDiscountResultDto shippingResult,
	long totalDiscount,
	long finalAmount
) {}
