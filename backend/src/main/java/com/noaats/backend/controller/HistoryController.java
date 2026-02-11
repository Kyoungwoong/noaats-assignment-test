package com.noaats.backend.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.noaats.backend.api.ApiEnvelope;
import com.noaats.backend.api.ErrorCode;
import com.noaats.backend.auth.UserPrincipal;
import com.noaats.backend.dto.history.HistoryDetailDto;
import com.noaats.backend.dto.history.HistorySummaryDto;
import com.noaats.backend.exception.ApiException;
import com.noaats.backend.history.HistoryService;

@RestController
@Tag(name = "History", description = "User promo history endpoints")
public class HistoryController {
	private final HistoryService historyService;

	public HistoryController(HistoryService historyService) {
		this.historyService = historyService;
	}

	@GetMapping("/api/history")
	@Operation(summary = "List history", description = "Lists promo calculation history for the current user.")
	public ApiEnvelope<List<HistorySummaryDto>> list() {
		Long userId = requireUserId();
		return ApiEnvelope.ok(historyService.listSummaries(userId));
	}

	@GetMapping("/api/history/{id}")
	@Operation(summary = "History detail", description = "Returns a history record detail.")
	public ApiEnvelope<HistoryDetailDto> detail(@PathVariable("id") Long id) {
		Long userId = requireUserId();
		return ApiEnvelope.ok(historyService.getDetail(userId, id));
	}

	private Long requireUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
			throw new ApiException(ErrorCode.UNAUTHORIZED, "Login required");
		}
		return principal.id();
	}
}
