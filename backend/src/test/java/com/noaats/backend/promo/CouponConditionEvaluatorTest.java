package com.noaats.backend.promo;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CouponConditionEvaluatorTest {

	@Test
	void returnsCategoryExcludedWhenCategoryMatches() {
		PromoCalculationContext context = new PromoCalculationContext(List.of("SHOES"), PaymentMethod.CARD, Instant.now());

		CouponConditionReason reason = CouponConditionEvaluator.evaluate(
			List.of("SHOES"),
			null,
			null,
			null,
			context
		);

		assertEquals(CouponConditionReason.CATEGORY_EXCLUDED, reason);
	}

	@Test
	void returnsPaymentMethodNotAllowedWhenMethodIsNotAllowed() {
		PromoCalculationContext context = new PromoCalculationContext(List.of("FOOD"), PaymentMethod.BANK, Instant.now());

		CouponConditionReason reason = CouponConditionEvaluator.evaluate(
			null,
			List.of(PaymentMethod.CARD),
			null,
			null,
			context
		);

		assertEquals(CouponConditionReason.PAYMENT_METHOD_NOT_ALLOWED, reason);
	}

	@Test
	void returnsCouponExpiredWhenBeforeValidFrom() {
		Instant now = Instant.parse("2026-02-12T00:00:00Z");
		PromoCalculationContext context = new PromoCalculationContext(null, PaymentMethod.CARD, now);

		CouponConditionReason reason = CouponConditionEvaluator.evaluate(
			null,
			null,
			now.plusSeconds(3600),
			null,
			context
		);

		assertEquals(CouponConditionReason.COUPON_EXPIRED, reason);
	}

	@Test
	void returnsNullWhenAllConditionsPass() {
		Instant now = Instant.parse("2026-02-12T00:00:00Z");
		PromoCalculationContext context = new PromoCalculationContext(List.of("HOME"), PaymentMethod.CARD, now);

		CouponConditionReason reason = CouponConditionEvaluator.evaluate(
			List.of("SHOES"),
			List.of(PaymentMethod.CARD),
			now.minusSeconds(10),
			now.plusSeconds(10),
			context
		);

		assertNull(reason);
	}
}
