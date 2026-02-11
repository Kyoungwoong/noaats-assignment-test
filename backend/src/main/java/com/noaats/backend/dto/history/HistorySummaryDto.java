package com.noaats.backend.dto.history;

import java.time.Instant;

public record HistorySummaryDto(
	Long id,
	Instant createdAt,
	long subtotal,
	long shippingFee,
	long finalAmount,
	long totalDiscount
) {}
