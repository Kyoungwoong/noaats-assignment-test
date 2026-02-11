package com.noaats.backend.promo;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceCouponCalculatorTest {

	private static PromoCalculationContext context() {
		return new PromoCalculationContext(List.of("SHOES"), PaymentMethod.CARD, Instant.parse("2026-02-11T00:00:00Z"));
	}

	@Test
	void s1_percent_coupon_applies_with_cap() {
		PriceCoupon coupon = new PriceCoupon(
			PriceCouponType.PERCENT,
			20,
			null,
			50_000L,
			8_000L,
			null,
			null,
			null,
			null
		);

		PriceDiscountResult result = PriceCouponCalculator.calculate(59_000L, 3_000L, coupon, context());

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
			null,
			null,
			null,
			null,
			null
		);

		PriceDiscountResult result = PriceCouponCalculator.calculate(59_000L, 3_000L, coupon, context());

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
			null,
			null,
			null,
			null,
			null
		);

		PriceDiscountResult result = PriceCouponCalculator.calculate(28_000L, 3_000L, coupon, context());

		assertFalse(result.applied());
		assertEquals(0L, result.discount());
		assertEquals(31_000L, result.finalAmount());
		assertEquals("MIN_SPEND_NOT_MET", result.reason());
	}
}
