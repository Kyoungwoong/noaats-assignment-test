package com.noaats.backend.controller;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.noaats.backend.dto.promo.PriceCouponDto;
import com.noaats.backend.dto.promo.PromoRequestDto;
import com.noaats.backend.dto.promo.ShippingCouponDto;
import com.noaats.backend.promo.PriceCouponType;
import com.noaats.backend.service.PromoService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PromoControllerTest {

	@Test
	void wraps_success_response() {
		PromoController controller = new PromoController(new PromoService());
		PromoRequestDto request = new PromoRequestDto(
			10_000L,
			3_000L,
			List.of(new PriceCouponDto(PriceCouponType.PERCENT, 10, null, 0L, 2000L)),
			List.of(new ShippingCouponDto(3000L, 3000L))
		);

		var response = controller.calculate(request);

		assertEquals(true, response.success());
		assertNotNull(response.data());
		assertNotNull(response.data().top3());
	}
}
