package com.noaats.backend.promo;

public record PriceCoupon(
	PriceCouponType type,
	Integer ratePercent,
	Long amount,
	Long minSpend,
	Long cap,
	java.util.List<String> excludedCategories,
	java.util.List<PaymentMethod> allowedPaymentMethods,
	java.time.Instant validFrom,
	java.time.Instant validTo
) {}
