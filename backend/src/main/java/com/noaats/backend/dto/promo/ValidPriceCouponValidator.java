package com.noaats.backend.dto.promo;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.noaats.backend.promo.PriceCouponType;

public final class ValidPriceCouponValidator implements ConstraintValidator<ValidPriceCoupon, PriceCouponDto> {

	@Override
	public boolean isValid(PriceCouponDto value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		if (value.type() == PriceCouponType.PERCENT) {
			return value.ratePercent() != null;
		}
		if (value.type() == PriceCouponType.FIXED) {
			return value.amount() != null;
		}
		return false;
	}
}
