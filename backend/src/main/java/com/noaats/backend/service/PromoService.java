package com.noaats.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.noaats.backend.dto.promo.PromoDtoMapper;
import com.noaats.backend.promo.PromoCalculator;
import com.noaats.backend.promo.PromoCombinationResult;

@Service
public class PromoService {

	public List<PromoCombinationResult> recommendTop3(PromoDtoMapper.PromoRequest request) {
		return PromoCalculator.recommendTop3(
			request.subtotal(),
			request.shippingFee(),
			request.items(),
			request.paymentMethod(),
			request.now(),
			request.priceCoupons(),
			request.shippingCoupons()
		);
	}
}
