package com.noaats.backend.controller;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.noaats.backend.dto.promo.CartItemDto;
import com.noaats.backend.dto.promo.PriceCouponDto;
import com.noaats.backend.dto.promo.PromoRequestDto;
import com.noaats.backend.dto.promo.ShippingCouponDto;
import com.noaats.backend.promo.PaymentMethod;
import com.noaats.backend.promo.PriceCouponType;
import com.noaats.backend.history.HistoryService;
import com.noaats.backend.service.PromoService;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class PromoControllerTest {

	@Test
	void wraps_success_response() {
		HistoryService historyService = Mockito.mock(HistoryService.class);
		PromoController controller = new PromoController(new PromoService(), historyService);
		PromoRequestDto request = new PromoRequestDto(
			10_000L,
			List.of(new CartItemDto("Item", 10_000L, 1, "SHOES")),
			3_000L,
			PaymentMethod.CARD,
			List.of(new PriceCouponDto(PriceCouponType.PERCENT, 10, null, 0L, 2000L, null, null, null, null)),
			List.of(new ShippingCouponDto(3000L, 3000L, null, null, null, null))
		);

		var response = controller.calculate(request);

		assertEquals(true, response.success());
		assertNotNull(response.data());
		assertNotNull(response.data().top3());
		verify(historyService, never()).saveHistory(Mockito.anyLong(), Mockito.any(), Mockito.any());
	}

	@Test
	void saves_history_when_user_present() {
		HistoryService historyService = Mockito.mock(HistoryService.class);
		PromoController controller = new PromoController(new PromoService(), historyService);
		PromoRequestDto request = new PromoRequestDto(
			10_000L,
			List.of(new CartItemDto("Item", 10_000L, 1, "SHOES")),
			3_000L,
			PaymentMethod.CARD,
			List.of(new PriceCouponDto(PriceCouponType.PERCENT, 10, null, 0L, 2000L, null, null, null, null)),
			List.of(new ShippingCouponDto(3000L, 3000L, null, null, null, null))
		);
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(new com.noaats.backend.auth.UserPrincipal(1L, "user"), null, List.of())
		);

		controller.calculate(request);

		verify(historyService).saveHistory(Mockito.eq(1L), Mockito.any(), Mockito.any());
		SecurityContextHolder.clearContext();
	}
}
