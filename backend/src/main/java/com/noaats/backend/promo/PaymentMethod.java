package com.noaats.backend.promo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payment method")
public enum PaymentMethod {
	@Schema(description = "Credit or debit card")
	CARD,
	@Schema(description = "Bank transfer")
	BANK,
	@Schema(description = "Kakao Pay")
	KAKAO
}
