package com.noaats.backend.promo;

import java.time.Instant;
import java.util.List;

public record PromoCalculationContext(
	List<String> categories,
	PaymentMethod paymentMethod,
	Instant now
) {}
