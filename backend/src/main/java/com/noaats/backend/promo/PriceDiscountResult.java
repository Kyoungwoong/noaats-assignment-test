package com.noaats.backend.promo;

public record PriceDiscountResult(
	boolean applied,
	long discount,
	long finalAmount,
	String reason,
	long shortfallAmount
) {}
