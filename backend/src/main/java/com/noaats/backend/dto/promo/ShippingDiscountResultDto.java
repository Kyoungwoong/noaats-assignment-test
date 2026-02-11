package com.noaats.backend.dto.promo;

public record ShippingDiscountResultDto(
	boolean applied,
	long discount,
	long remainingShippingFee,
	String reason
) {}
