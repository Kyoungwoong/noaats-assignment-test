package com.noaats.backend.promo;

import java.time.Instant;
import java.util.List;

public final class CouponConditionEvaluator {
	private CouponConditionEvaluator() {}

	public static CouponConditionReason evaluate(
		List<String> excludedCategories,
		List<PaymentMethod> allowedPaymentMethods,
		Instant validFrom,
		Instant validTo,
		PromoCalculationContext context
	) {
		if (context == null) {
			return null;
		}
		List<String> categories = context.categories();
		if (excludedCategories != null && categories != null) {
			for (String category : categories) {
				if (excludedCategories.contains(category)) {
					return CouponConditionReason.CATEGORY_EXCLUDED;
				}
			}
		}

		if (allowedPaymentMethods != null && !allowedPaymentMethods.isEmpty()) {
			PaymentMethod method = context.paymentMethod();
			if (method == null || !allowedPaymentMethods.contains(method)) {
				return CouponConditionReason.PAYMENT_METHOD_NOT_ALLOWED;
			}
		}

		Instant now = context.now();
		if (now != null) {
			if (validFrom != null && now.isBefore(validFrom)) {
				return CouponConditionReason.COUPON_EXPIRED;
			}
			if (validTo != null && now.isAfter(validTo)) {
				return CouponConditionReason.COUPON_EXPIRED;
			}
		}

		return null;
	}
}
