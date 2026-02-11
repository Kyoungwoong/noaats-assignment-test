package com.noaats.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.noaats.backend.promo.PriceCoupon;
import com.noaats.backend.promo.PromoCombinationResult;
import com.noaats.backend.promo.ShippingCoupon;
import com.noaats.backend.service.PromoService;

@RestController
public class PromoController {
	private final PromoService promoService;

	public PromoController(PromoService promoService) {
		this.promoService = promoService;
	}

	@PostMapping("/api/promo/calculate")
	public PromoResponse calculate(@RequestBody PromoRequest request) {
		List<PromoCombinationResult> top3 = promoService.recommendTop3(
			request.subtotal(),
			request.shippingFee(),
			request.priceCoupons(),
			request.shippingCoupons()
		);
		return new PromoResponse(top3);
	}

	public record PromoRequest(
		long subtotal,
		long shippingFee,
		List<PriceCoupon> priceCoupons,
		List<ShippingCoupon> shippingCoupons
	) {}

	public record PromoResponse(List<PromoCombinationResult> top3) {}
}
