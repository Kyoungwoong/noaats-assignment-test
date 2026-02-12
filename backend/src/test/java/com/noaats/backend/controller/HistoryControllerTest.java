package com.noaats.backend.controller;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.noaats.backend.auth.UserPrincipal;
import com.noaats.backend.dto.history.HistoryDetailDto;
import com.noaats.backend.dto.history.HistorySummaryDto;
import com.noaats.backend.exception.ApiException;
import com.noaats.backend.history.HistoryService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HistoryControllerTest {

	@AfterEach
	void clearContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void listsHistoryForUser() {
		HistoryService historyService = Mockito.mock(HistoryService.class);
		HistoryController controller = new HistoryController(historyService);
		HistorySummaryDto summary = new HistorySummaryDto(1L, Instant.now(), 10_000L, 3_000L, 9_000L, 4_000L);
		Mockito.when(historyService.listSummaries(1L)).thenReturn(List.of(summary));
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(new UserPrincipal(1L, "user"), null, List.of())
		);

		var response = controller.list();

		assertEquals(true, response.success());
		assertEquals(1, response.data().size());
	}

	@Test
	void returnsHistoryDetailForUser() {
		HistoryService historyService = Mockito.mock(HistoryService.class);
		HistoryController controller = new HistoryController(historyService);
		HistoryDetailDto detail = new HistoryDetailDto(
			2L,
			Instant.now(),
			10_000L,
			3_000L,
			9_000L,
			4_000L,
			JsonNodeFactory.instance.objectNode(),
			JsonNodeFactory.instance.objectNode()
		);
		Mockito.when(historyService.getDetail(1L, 2L)).thenReturn(detail);
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(new UserPrincipal(1L, "user"), null, List.of())
		);

		var response = controller.detail(2L);

		assertEquals(true, response.success());
		assertEquals(2L, response.data().id());
	}

	@Test
	void throws_when_user_not_authenticated() {
		HistoryService historyService = Mockito.mock(HistoryService.class);
		HistoryController controller = new HistoryController(historyService);

		assertThrows(ApiException.class, controller::list);
	}
}
