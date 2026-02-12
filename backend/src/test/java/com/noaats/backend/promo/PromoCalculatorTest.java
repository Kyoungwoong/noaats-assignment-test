package com.noaats.backend.promo;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PromoCalculatorTest {

	@Test
	void recommends_top3_by_final_amount_discount_and_reason() {
		long subtotal = 10_000L;
		long shippingFee = 3_000L;
		List<CartItem> items = List.of(new CartItem("Item", 10_000L, 1, "SHOES"));

		List<PriceCoupon> priceCoupons = List.of(
			new PriceCoupon(PriceCouponType.PERCENT, 10, null, 0L, 2_000L, null, null, null, null),
			new PriceCoupon(PriceCouponType.FIXED, null, 1_500L, 0L, null, null, null, null, null)
		);
		List<ShippingCoupon> shippingCoupons = List.of(
			new ShippingCoupon(3_000L, 3_000L, null, null, null, null)
		);

		List<PromoCombinationResult> top3 = PromoCalculator.recommendTop3(
			subtotal,
			shippingFee,
			items,
			PaymentMethod.CARD,
			Instant.parse("2026-02-11T00:00:00Z"),
			priceCoupons,
			shippingCoupons
		);

		assertEquals(3, top3.size());

		PromoCombinationResult first = top3.get(0);
		PromoCombinationResult second = top3.get(1);
		PromoCombinationResult third = top3.get(2);

		assertEquals(8_500L, first.finalAmount());
		assertEquals(9_000L, second.finalAmount());
		assertEquals(10_000L, third.finalAmount());

		assertEquals("결제액 최소", first.reason());
		assertEquals("총할인액 우선", second.reason());
		assertEquals("입력 순서 유지", third.reason());

		assertEquals(0.45, first.discountRateBySubtotal(), 0.0001);
		assertEquals(0.3461, first.discountRateByTotal(), 0.0001);

		assertNotNull(first.priceCoupon());
		assertNotNull(first.shippingCoupon());
	}

	@Test
	void handles_null_items_and_null_coupons() {
		List<PromoCombinationResult> results = PromoCalculator.recommendTop3(
			10_000L,
			3_000L,
			null,
			PaymentMethod.CARD,
			Instant.parse("2026-02-11T00:00:00Z"),
			null,
			null
		);

		assertEquals(1, results.size());
		PromoCombinationResult result = results.get(0);
		assertEquals(13_000L, result.finalAmount());
		assertNull(result.priceCoupon());
		assertNull(result.shippingCoupon());
	}
}
