package com.noaats.backend.dto.promo;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidPriceCouponValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface ValidPriceCoupon {
	String message() default "Invalid price coupon fields";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
