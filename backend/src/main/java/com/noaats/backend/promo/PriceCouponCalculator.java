package com.noaats.backend.promo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class PriceCouponCalculator {
	private PriceCouponCalculator() {}

	public static PriceDiscountResult calculate(
		long subtotal,
		long shippingFee,
		PriceCoupon coupon,
		PromoCalculationContext context
	) {
		Objects.requireNonNull(coupon, "coupon");

		CouponConditionReason conditionReason = CouponConditionEvaluator.evaluate(
			coupon.excludedCategories(),
			coupon.allowedPaymentMethods(),
			coupon.validFrom(),
			coupon.validTo(),
			context
		);
		if (conditionReason != null) {
			return new PriceDiscountResult(false, 0L, subtotal + shippingFee, conditionReason.name());
		}

		Long minSpend = coupon.minSpend();
		if (minSpend != null && subtotal < minSpend) {
			return new PriceDiscountResult(false, 0L, subtotal + shippingFee, "MIN_SPEND_NOT_MET");
		}

		long raw = calculateRawDiscount(subtotal, coupon);
		long cap = coupon.cap() == null ? Long.MAX_VALUE : coupon.cap();
		long clamped = Math.min(raw, Math.min(subtotal, cap));
		long discount = Math.max(0L, clamped);
		long finalAmount = (subtotal - discount) + shippingFee;

		return new PriceDiscountResult(true, discount, finalAmount, null);
	}

	private static long calculateRawDiscount(long subtotal, PriceCoupon coupon) {
		if (coupon.type() == PriceCouponType.PERCENT) {
			Integer ratePercent = coupon.ratePercent();
			if (ratePercent == null) {
				throw new IllegalArgumentException("ratePercent is required for PERCENT coupon");
			}
			BigDecimal raw = BigDecimal.valueOf(subtotal)
				.multiply(BigDecimal.valueOf(ratePercent))
				.divide(BigDecimal.valueOf(100), 0, RoundingMode.DOWN);
			return raw.longValueExact();
		}

		if (coupon.type() == PriceCouponType.FIXED) {
			Long amount = coupon.amount();
			if (amount == null) {
				throw new IllegalArgumentException("amount is required for FIXED coupon");
			}
			return amount;
		}

		throw new IllegalArgumentException("Unsupported coupon type: " + coupon.type());
	}
}
