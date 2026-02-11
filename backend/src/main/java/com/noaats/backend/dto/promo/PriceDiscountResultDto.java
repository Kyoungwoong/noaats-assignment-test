package com.noaats.backend.dto.promo;

public record PriceDiscountResultDto(
	boolean applied,
	long discount,
	long finalAmount,
	String reason
) {}
