package com.noaats.backend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.noaats.backend.dto.promo.PromoDtoMapper;
import com.noaats.backend.dto.promo.PromoRequestDto;
import com.noaats.backend.dto.promo.PromoResponseDto;
import com.noaats.backend.service.PromoService;

@RestController
public class PromoController {
	private final PromoService promoService;

	public PromoController(PromoService promoService) {
		this.promoService = promoService;
	}

	@PostMapping("/api/promo/calculate")
	public PromoResponseDto calculate(@Valid @RequestBody PromoRequestDto request) {
		PromoDtoMapper.PromoRequest domainRequest = PromoDtoMapper.toDomainRequest(request);
		return PromoDtoMapper.toResponse(promoService.recommendTop3(
			domainRequest.subtotal(),
			domainRequest.shippingFee(),
			domainRequest.priceCoupons(),
			domainRequest.shippingCoupons()
		));
	}
}
