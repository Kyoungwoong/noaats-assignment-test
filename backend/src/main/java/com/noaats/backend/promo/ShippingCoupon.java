package com.noaats.backend.promo;

public record ShippingCoupon(
	Long shippingDiscount,
	Long cap,
	java.util.List<String> excludedCategories,
	java.util.List<PaymentMethod> allowedPaymentMethods,
	java.time.Instant validFrom,
	java.time.Instant validTo
) {}
