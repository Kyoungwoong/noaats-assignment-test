package com.noaats.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.noaats.backend.promo.PriceCoupon;
import com.noaats.backend.promo.PromoCalculator;
import com.noaats.backend.promo.PromoCombinationResult;
import com.noaats.backend.promo.ShippingCoupon;

@Service
public class PromoService {

	public List<PromoCombinationResult> recommendTop3(
		long subtotal,
		long shippingFee,
		List<PriceCoupon> priceCoupons,
		List<ShippingCoupon> shippingCoupons
	) {
		return PromoCalculator.recommendTop3(subtotal, shippingFee, priceCoupons, shippingCoupons);
	}
}
