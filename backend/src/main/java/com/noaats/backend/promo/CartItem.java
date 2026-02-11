package com.noaats.backend.promo;

public record CartItem(
	String name,
	long price,
	int quantity,
	String category
) {}
