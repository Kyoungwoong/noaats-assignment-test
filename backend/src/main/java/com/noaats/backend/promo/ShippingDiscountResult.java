package com.noaats.backend.promo;

public record ShippingDiscountResult(
	boolean applied,
	long discount,
	long remainingShippingFee,
	String reason
) {}
