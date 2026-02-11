package com.noaats.backend.promo;

import java.util.Objects;

public final class ShippingCouponCalculator {
	private ShippingCouponCalculator() {}

	public static ShippingDiscountResult calculate(long shippingFee, ShippingCoupon coupon) {
		Objects.requireNonNull(coupon, "coupon");
		Long discountAmount = coupon.shippingDiscount();
		if (discountAmount == null) {
			throw new IllegalArgumentException("shippingDiscount is required for shipping coupon");
		}

		long cap = coupon.cap() == null ? Long.MAX_VALUE : coupon.cap();
		long clamped = Math.min(discountAmount, Math.min(shippingFee, cap));
		long discount = Math.max(0L, clamped);
		long remaining = Math.max(0L, shippingFee - discount);
		boolean applied = discount > 0L;

		return new ShippingDiscountResult(applied, discount, remaining, null);
	}
}
