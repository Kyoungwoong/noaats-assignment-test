package com.noaats.backend.promo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShippingCouponCalculatorTest {

	@Test
	void s2_shipping_coupon_applies_and_clamps_to_fee() {
		ShippingCoupon coupon = new ShippingCoupon(3_000L, 3_000L);

		ShippingDiscountResult result = ShippingCouponCalculator.calculate(3_000L, coupon);

		assertTrue(result.applied());
		assertEquals(3_000L, result.discount());
		assertEquals(0L, result.remainingShippingFee());
	}

	@Test
	void shipping_coupon_without_cap_clamps_to_fee() {
		ShippingCoupon coupon = new ShippingCoupon(5_000L, null);

		ShippingDiscountResult result = ShippingCouponCalculator.calculate(3_000L, coupon);

		assertTrue(result.applied());
		assertEquals(3_000L, result.discount());
		assertEquals(0L, result.remainingShippingFee());
	}
}
