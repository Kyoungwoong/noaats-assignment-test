package com.noaats.backend.dto.promo;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.noaats.backend.promo.PriceCouponType;
import com.noaats.backend.promo.PromoCombinationResult;
import com.noaats.backend.promo.PriceDiscountResult;
import com.noaats.backend.promo.ShippingDiscountResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PromoDtoMapperTest {

	@Test
	void maps_request_to_domain_and_recomputes_subtotal() {
		PromoRequestDto request = new PromoRequestDto(
			0L,
			List.of(new CartItemDto("Item", 5_000L, 2, "FOOD")),
			3_000L,
			null,
			List.of(new PriceCouponDto(PriceCouponType.FIXED, null, 1_000L, 0L, null, null, null, null, null)),
			List.of(new ShippingCouponDto(3_000L, 3_000L, null, null, null, null))
		);

		PromoDtoMapper.PromoRequest domain = PromoDtoMapper.toDomainRequest(request);

		assertEquals(10_000L, domain.subtotal());
		assertEquals(3_000L, domain.shippingFee());
		assertEquals(1, domain.priceCoupons().size());
	}

	@Test
	void throws_when_percent_coupon_missing_rate() {
		PriceCouponDto coupon = new PriceCouponDto(
			PriceCouponType.PERCENT,
			null,
			null,
			0L,
			null,
			null,
			null,
			null,
			null
		);

		PromoRequestDto request = new PromoRequestDto(
			10_000L,
			List.of(new CartItemDto("Item", 10_000L, 1, "FOOD")),
			3_000L,
			null,
			List.of(coupon),
			List.of()
		);

		assertThrows(IllegalArgumentException.class, () -> PromoDtoMapper.toDomainRequest(request));
	}

	@Test
	void maps_response_with_null_coupons() {
		PromoCombinationResult result = new PromoCombinationResult(
			null,
			null,
			new PriceDiscountResult(false, 0L, 10_000L, null, 0L),
			new ShippingDiscountResult(false, 0L, 3_000L, null),
			0L,
			13_000L,
			"reason",
			0.0,
			0.0
		);

		PromoResponseDto response = PromoDtoMapper.toResponse(List.of(result));

		assertEquals(1, response.top3().size());
		assertNull(response.top3().getFirst().priceCoupon());
		assertNull(response.top3().getFirst().shippingCoupon());
	}

	@Test
	void maps_request_with_empty_coupon_lists() {
		PromoRequestDto request = new PromoRequestDto(
			0L,
			List.of(new CartItemDto("Item", 1_000L, 1, "FOOD")),
			0L,
			null,
			List.of(),
			List.of()
		);

		PromoDtoMapper.PromoRequest domain = PromoDtoMapper.toDomainRequest(request);

		assertEquals(1_000L, domain.subtotal());
		assertEquals(0, domain.priceCoupons().size());
		assertEquals(0, domain.shippingCoupons().size());
	}
}
