package com.noaats.backend.promo;

public record PromoCombinationResult(
	PriceCoupon priceCoupon,
	ShippingCoupon shippingCoupon,
	PriceDiscountResult priceResult,
	ShippingDiscountResult shippingResult,
	long totalDiscount,
	long finalAmount
) {}
