package com.noaats.backend.history;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noaats.backend.api.ErrorCode;
import com.noaats.backend.auth.UserEntity;
import com.noaats.backend.auth.UserRepository;
import com.noaats.backend.dto.history.HistoryDetailDto;
import com.noaats.backend.dto.history.HistorySummaryDto;
import com.noaats.backend.dto.promo.CartItemDto;
import com.noaats.backend.dto.promo.PriceCouponDto;
import com.noaats.backend.dto.promo.PromoRequestDto;
import com.noaats.backend.dto.promo.PromoResponseDto;
import com.noaats.backend.dto.promo.PromoCombinationResultDto;
import com.noaats.backend.dto.promo.PriceDiscountResultDto;
import com.noaats.backend.dto.promo.ShippingCouponDto;
import com.noaats.backend.dto.promo.ShippingDiscountResultDto;
import com.noaats.backend.exception.ApiException;
import com.noaats.backend.promo.PaymentMethod;
import com.noaats.backend.promo.PriceCouponType;

class HistoryServiceTest {

	@Test
	void list_summaries_maps_entities() {
		PromoHistoryRepository repo = mock(PromoHistoryRepository.class);
		UserRepository userRepo = mock(UserRepository.class);
		ObjectMapper mapper = new ObjectMapper();
		HistoryService service = new HistoryService(repo, userRepo, mapper);
		UserEntity user = new UserEntity("user", "hash");
		PromoHistory history = new PromoHistory(user, 1000L, 200L, 1100L, 100L, "{}", "{}");
		when(repo.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(history));

		List<HistorySummaryDto> list = service.listSummaries(1L);

		assertEquals(1, list.size());
		assertEquals(1100L, list.get(0).finalAmount());
	}

	@Test
	void get_detail_requires_matching_user() {
		PromoHistoryRepository repo = mock(PromoHistoryRepository.class);
		UserRepository userRepo = mock(UserRepository.class);
		ObjectMapper mapper = new ObjectMapper();
		HistoryService service = new HistoryService(repo, userRepo, mapper);
		when(repo.findByIdAndUserId(1L, 2L)).thenReturn(Optional.empty());

		ApiException ex = assertThrows(ApiException.class, () -> service.getDetail(2L, 1L));
		assertEquals(ErrorCode.NOT_FOUND, ex.errorCode());
	}

	@Test
	void save_history_serializes_request_and_response() {
		PromoHistoryRepository repo = mock(PromoHistoryRepository.class);
		UserRepository userRepo = mock(UserRepository.class);
		ObjectMapper mapper = new ObjectMapper();
		HistoryService service = new HistoryService(repo, userRepo, mapper);
		UserEntity user = new UserEntity("user", "hash");
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(repo.save(any(PromoHistory.class))).thenAnswer(inv -> inv.getArgument(0));

		PromoRequestDto request = new PromoRequestDto(
			1000L,
			List.of(new CartItemDto("Item", 1000L, 1, "SHOES")),
			200L,
			PaymentMethod.CARD,
			List.of(new PriceCouponDto(PriceCouponType.FIXED, null, 100L, 0L, 200L, null, null, null, null)),
			List.of(new ShippingCouponDto(200L, 200L, null, null, null, null))
		);
		PromoResponseDto response = new PromoResponseDto(
			List.of(new PromoCombinationResultDto(
				null,
				null,
				new PriceDiscountResultDto(true, 100L, 900L, null, 0L),
				new ShippingDiscountResultDto(true, 200L, 0L, null),
				300L,
				900L,
				"ok",
				0.3,
				0.25
			))
		);

		service.saveHistory(1L, request, response);

		ArgumentCaptor<PromoHistory> captor = ArgumentCaptor.forClass(PromoHistory.class);
		verify(repo).save(captor.capture());
		PromoHistory saved = captor.getValue();
		assertEquals(1000L, saved.getSubtotal());
		assertEquals(900L, saved.getFinalAmount());
		assertEquals(true, saved.getRequestJson().contains("\"subtotal\":1000"));
	}

	@Test
	void save_history_when_user_missing_throws() {
		PromoHistoryRepository repo = mock(PromoHistoryRepository.class);
		UserRepository userRepo = mock(UserRepository.class);
		ObjectMapper mapper = new ObjectMapper();
		HistoryService service = new HistoryService(repo, userRepo, mapper);
		when(userRepo.findById(1L)).thenReturn(Optional.empty());

		PromoRequestDto request = new PromoRequestDto(
			1000L,
			List.of(new CartItemDto("Item", 1000L, 1, "SHOES")),
			200L,
			PaymentMethod.CARD,
			List.of(),
			List.of()
		);
		PromoResponseDto response = new PromoResponseDto(List.of());

		ApiException ex = assertThrows(ApiException.class, () -> service.saveHistory(1L, request, response));
		assertEquals(ErrorCode.UNAUTHORIZED, ex.errorCode());
	}

	@Test
	void save_history_handles_empty_top3() {
		PromoHistoryRepository repo = mock(PromoHistoryRepository.class);
		UserRepository userRepo = mock(UserRepository.class);
		ObjectMapper mapper = new ObjectMapper();
		HistoryService service = new HistoryService(repo, userRepo, mapper);
		UserEntity user = new UserEntity("user", "hash");
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(repo.save(any(PromoHistory.class))).thenAnswer(inv -> inv.getArgument(0));

		PromoRequestDto request = new PromoRequestDto(
			1000L,
			List.of(new CartItemDto("Item", 1000L, 1, "SHOES")),
			200L,
			PaymentMethod.CARD,
			List.of(),
			List.of()
		);
		PromoResponseDto response = new PromoResponseDto(List.of());

		service.saveHistory(1L, request, response);

		ArgumentCaptor<PromoHistory> captor = ArgumentCaptor.forClass(PromoHistory.class);
		verify(repo).save(captor.capture());
		PromoHistory saved = captor.getValue();
		assertEquals(1200L, saved.getFinalAmount());
		assertEquals(0L, saved.getTotalDiscount());
	}

	@Test
	void get_detail_parses_json() {
		PromoHistoryRepository repo = mock(PromoHistoryRepository.class);
		UserRepository userRepo = mock(UserRepository.class);
		ObjectMapper mapper = new ObjectMapper();
		HistoryService service = new HistoryService(repo, userRepo, mapper);
		UserEntity user = new UserEntity("user", "hash");
		PromoHistory history = new PromoHistory(user, 1000L, 200L, 1100L, 100L, "{\"a\":1}", "{\"b\":2}");
		when(repo.findByIdAndUserId(5L, 1L)).thenReturn(Optional.of(history));

		HistoryDetailDto detail = service.getDetail(1L, 5L);

		assertEquals(1, detail.request().get("a").asInt());
		assertEquals(2, detail.response().get("b").asInt());
	}
}
