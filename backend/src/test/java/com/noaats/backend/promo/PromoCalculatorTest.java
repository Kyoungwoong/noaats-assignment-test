package com.noaats.backend.promo;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PromoCalculatorTest {

	@Test
	void recommends_top3_by_final_amount_then_discount_then_input_order() {
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

		assertNotNull(first.priceCoupon());
		assertNotNull(first.shippingCoupon());
	}
}
