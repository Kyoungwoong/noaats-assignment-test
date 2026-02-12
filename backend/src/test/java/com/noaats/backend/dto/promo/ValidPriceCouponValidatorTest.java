package com.noaats.backend.dto.promo;

import org.junit.jupiter.api.Test;

import com.noaats.backend.promo.PriceCouponType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidPriceCouponValidatorTest {

	@Test
	void acceptsNullCoupon() {
		ValidPriceCouponValidator validator = new ValidPriceCouponValidator();

		assertTrue(validator.isValid(null, null));
	}

	@Test
	void requiresRateForPercentCoupon() {
		ValidPriceCouponValidator validator = new ValidPriceCouponValidator();
		PriceCouponDto coupon = new PriceCouponDto(
			PriceCouponType.PERCENT,
			null,
			null,
			0L,
			0L,
			null,
			null,
			null,
			null
		);

		assertFalse(validator.isValid(coupon, null));
	}

	@Test
	void requiresAmountForFixedCoupon() {
		ValidPriceCouponValidator validator = new ValidPriceCouponValidator();
		PriceCouponDto coupon = new PriceCouponDto(
			PriceCouponType.FIXED,
			null,
			null,
			0L,
			0L,
			null,
			null,
			null,
			null
		);

		assertFalse(validator.isValid(coupon, null));
	}

	@Test
	void acceptsValidPercentCoupon() {
		ValidPriceCouponValidator validator = new ValidPriceCouponValidator();
		PriceCouponDto coupon = new PriceCouponDto(
			PriceCouponType.PERCENT,
			10,
			null,
			0L,
			0L,
			null,
			null,
			null,
			null
		);

		assertTrue(validator.isValid(coupon, null));
	}

	@Test
	void acceptsValidFixedCoupon() {
		ValidPriceCouponValidator validator = new ValidPriceCouponValidator();
		PriceCouponDto coupon = new PriceCouponDto(
			PriceCouponType.FIXED,
			null,
			1000L,
			0L,
			0L,
			null,
			null,
			null,
			null
		);

		assertTrue(validator.isValid(coupon, null));
	}
}
