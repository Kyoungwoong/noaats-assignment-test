package com.noaats.backend.promo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceCouponCalculatorTest {

	@Test
	void s1_percent_coupon_applies_with_cap() {
		PriceCoupon coupon = new PriceCoupon(
			PriceCouponType.PERCENT,
			20,
			null,
			50_000L,
			8_000L
		);

		PriceDiscountResult result = PriceCouponCalculator.calculate(59_000L, 3_000L, coupon);

		assertTrue(result.applied());
		assertEquals(8_000L, result.discount());
		assertEquals(54_000L, result.finalAmount());
	}

	@Test
	void s1_fixed_coupon_applies() {
		PriceCoupon coupon = new PriceCoupon(
			PriceCouponType.FIXED,
			null,
			7_000L,
			40_000L,
			null
		);

		PriceDiscountResult result = PriceCouponCalculator.calculate(59_000L, 3_000L, coupon);

		assertTrue(result.applied());
		assertEquals(7_000L, result.discount());
		assertEquals(55_000L, result.finalAmount());
	}

	@Test
	void min_spend_not_met_returns_zero_discount() {
		PriceCoupon coupon = new PriceCoupon(
			PriceCouponType.FIXED,
			null,
			6_000L,
			30_000L,
			null
		);

		PriceDiscountResult result = PriceCouponCalculator.calculate(28_000L, 3_000L, coupon);

		assertFalse(result.applied());
		assertEquals(0L, result.discount());
		assertEquals(31_000L, result.finalAmount());
		assertEquals("MIN_SPEND_NOT_MET", result.reason());
	}
}
