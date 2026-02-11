package com.noaats.backend.dto.promo;

import io.swagger.v3.oas.annotations.media.Schema;

public record PromoCombinationResultDto(
	@Schema(description = "Applied price coupon, null if not used")
	PriceCouponDto priceCoupon,
	@Schema(description = "Applied shipping coupon, null if not used")
	ShippingCouponDto shippingCoupon,
	@Schema(description = "Price coupon result details")
	PriceDiscountResultDto priceResult,
	@Schema(description = "Shipping coupon result details")
	ShippingDiscountResultDto shippingResult,
	@Schema(description = "Total discount amount", example = "10000")
	long totalDiscount,
	@Schema(description = "Final amount to pay", example = "54000")
	long finalAmount,
	@Schema(description = "Recommendation reason", example = "결제액 최소")
	String reason
) {}
