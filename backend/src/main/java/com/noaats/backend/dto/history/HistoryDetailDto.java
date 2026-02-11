package com.noaats.backend.dto.history;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;

public record HistoryDetailDto(
	Long id,
	Instant createdAt,
	long subtotal,
	long shippingFee,
	long finalAmount,
	long totalDiscount,
	JsonNode request,
	JsonNode response
) {}
