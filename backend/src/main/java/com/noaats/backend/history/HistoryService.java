package com.noaats.backend.history;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noaats.backend.api.ErrorCode;
import com.noaats.backend.auth.UserEntity;
import com.noaats.backend.auth.UserRepository;
import com.noaats.backend.dto.history.HistoryDetailDto;
import com.noaats.backend.dto.history.HistorySummaryDto;
import com.noaats.backend.dto.promo.PromoRequestDto;
import com.noaats.backend.dto.promo.PromoResponseDto;
import com.noaats.backend.exception.ApiException;

@Service
public class HistoryService {
	private final PromoHistoryRepository historyRepository;
	private final UserRepository userRepository;
	private final ObjectMapper objectMapper;

	public HistoryService(PromoHistoryRepository historyRepository, UserRepository userRepository, ObjectMapper objectMapper) {
		this.historyRepository = historyRepository;
		this.userRepository = userRepository;
		this.objectMapper = objectMapper;
	}

	public void saveHistory(Long userId, PromoRequestDto request, PromoResponseDto response) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED, "User not found"));
		long subtotal = request.subtotal();
		long shippingFee = request.shippingFee();
		long finalAmount = response.top3().isEmpty() ? subtotal + shippingFee : response.top3().getFirst().finalAmount();
		long totalDiscount = response.top3().isEmpty() ? 0L : response.top3().getFirst().totalDiscount();
		PromoHistory history = new PromoHistory(
			user,
			subtotal,
			shippingFee,
			finalAmount,
			totalDiscount,
			writeJson(request),
			writeJson(response)
		);
		historyRepository.save(history);
	}

	public List<HistorySummaryDto> listSummaries(Long userId) {
		return historyRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
			.map(history -> new HistorySummaryDto(
				history.getId(),
				history.getCreatedAt(),
				history.getSubtotal(),
				history.getShippingFee(),
				history.getFinalAmount(),
				history.getTotalDiscount()
			))
			.toList();
	}

	public HistoryDetailDto getDetail(Long userId, Long historyId) {
		PromoHistory history = historyRepository.findByIdAndUserId(historyId, userId)
			.orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "History not found"));
		return new HistoryDetailDto(
			history.getId(),
			history.getCreatedAt(),
			history.getSubtotal(),
			history.getShippingFee(),
			history.getFinalAmount(),
			history.getTotalDiscount(),
			readJson(history.getRequestJson()),
			readJson(history.getResponseJson())
		);
	}

	private String writeJson(Object value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException ex) {
			throw new ApiException(ErrorCode.INTERNAL_ERROR, "Failed to serialize history");
		}
	}

	private JsonNode readJson(String json) {
		try {
			return objectMapper.readTree(json);
		} catch (JsonProcessingException ex) {
			throw new ApiException(ErrorCode.INTERNAL_ERROR, "Failed to parse history");
		}
	}
}
