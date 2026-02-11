package com.noaats.backend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.noaats.backend.api.ApiEnvelopeSchema;
import com.noaats.backend.api.ApiErrorResponseSchema;
import com.noaats.backend.api.ApiEnvelope;
import com.noaats.backend.dto.promo.PromoDtoMapper;
import com.noaats.backend.dto.promo.PromoRequestDto;
import com.noaats.backend.dto.promo.PromoResponseDto;
import com.noaats.backend.service.PromoService;

@RestController
@Tag(name = "Promo", description = "Promo calculation endpoints")
public class PromoController {
	private final PromoService promoService;

	public PromoController(PromoService promoService) {
		this.promoService = promoService;
	}

	@PostMapping("/api/promo/calculate")
	@Operation(
		summary = "Calculate promo combinations",
		description = "Calculates discounts and returns Top3 combinations based on policy rules."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Top3 combinations computed",
		content = @Content(schema = @Schema(implementation = ApiEnvelopeSchema.class))
	)
	@ApiResponse(
		responseCode = "400",
		description = "Validation error",
		content = @Content(schema = @Schema(implementation = ApiErrorResponseSchema.class))
	)
	public ApiEnvelope<PromoResponseDto> calculate(@Valid @RequestBody PromoRequestDto request) {
		PromoDtoMapper.PromoRequest domainRequest = PromoDtoMapper.toDomainRequest(request);
		PromoResponseDto response = PromoDtoMapper.toResponse(promoService.recommendTop3(
			domainRequest.subtotal(),
			domainRequest.shippingFee(),
			domainRequest.priceCoupons(),
			domainRequest.shippingCoupons()
		));
		return ApiEnvelope.ok(response);
	}
}
