package com.noaats.backend.promo;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShippingCouponCalculatorTest {

	private static PromoCalculationContext context() {
		return new PromoCalculationContext(List.of("SHOES"), PaymentMethod.CARD, Instant.parse("2026-02-11T00:00:00Z"));
	}

	@Test
	void s2_shipping_coupon_applies_and_clamps_to_fee() {
		ShippingCoupon coupon = new ShippingCoupon(3_000L, 3_000L, null, null, null, null);

		ShippingDiscountResult result = ShippingCouponCalculator.calculate(3_000L, coupon, context());

		assertTrue(result.applied());
		assertEquals(3_000L, result.discount());
		assertEquals(0L, result.remainingShippingFee());
	}

	@Test
	void shipping_coupon_without_cap_clamps_to_fee() {
		ShippingCoupon coupon = new ShippingCoupon(5_000L, null, null, null, null, null);

		ShippingDiscountResult result = ShippingCouponCalculator.calculate(3_000L, coupon, context());

		assertTrue(result.applied());
		assertEquals(3_000L, result.discount());
		assertEquals(0L, result.remainingShippingFee());
	}

	@Test
	void returns_reason_when_payment_method_not_allowed() {
		ShippingCoupon coupon = new ShippingCoupon(3_000L, 3_000L, null, List.of(PaymentMethod.BANK), null, null);

		ShippingDiscountResult result = ShippingCouponCalculator.calculate(3_000L, coupon, context());

		assertFalse(result.applied());
		assertEquals("PAYMENT_METHOD_NOT_ALLOWED", result.reason());
	}

	@Test
	void shipping_coupon_zero_fee_results_in_not_applied() {
		ShippingCoupon coupon = new ShippingCoupon(3_000L, 3_000L, null, null, null, null);

		ShippingDiscountResult result = ShippingCouponCalculator.calculate(0L, coupon, context());

		assertFalse(result.applied());
		assertEquals(0L, result.discount());
	}

	@Test
	void throws_when_shipping_discount_missing() {
		ShippingCoupon coupon = new ShippingCoupon(null, 3_000L, null, null, null, null);

		assertThrows(IllegalArgumentException.class, () -> ShippingCouponCalculator.calculate(3_000L, coupon, context()));
	}
}
