package com.noaats.backend.promo;

public record PriceCoupon(
	PriceCouponType type,
	Integer ratePercent,
	Long amount,
	Long minSpend,
	Long cap
) {}
