package com.noaats.backend.dto.promo;

import java.util.List;
import java.util.Objects;

import com.noaats.backend.promo.PriceCoupon;
import com.noaats.backend.promo.PriceCouponType;
import com.noaats.backend.promo.PriceDiscountResult;
import com.noaats.backend.promo.PromoCombinationResult;
import com.noaats.backend.promo.ShippingCoupon;
import com.noaats.backend.promo.ShippingDiscountResult;

public final class PromoDtoMapper {
	private PromoDtoMapper() {}

	public static PromoRequest toDomainRequest(PromoRequestDto request) {
		Objects.requireNonNull(request, "request");
		List<PriceCoupon> priceCoupons = request.priceCoupons().stream()
			.map(PromoDtoMapper::toDomain)
			.toList();
		List<ShippingCoupon> shippingCoupons = request.shippingCoupons().stream()
			.map(PromoDtoMapper::toDomain)
			.toList();
		return new PromoRequest(
			request.subtotal(),
			request.shippingFee(),
			priceCoupons,
			shippingCoupons
		);
	}

	public static PromoResponseDto toResponse(List<PromoCombinationResult> results) {
		List<PromoCombinationResultDto> top3 = results.stream()
			.map(PromoDtoMapper::toDto)
			.toList();
		return new PromoResponseDto(top3);
	}

	private static PriceCoupon toDomain(PriceCouponDto dto) {
		Objects.requireNonNull(dto, "priceCoupon");
		PriceCouponType type = dto.type();
		if (type == PriceCouponType.PERCENT && dto.ratePercent() == null) {
			throw new IllegalArgumentException("ratePercent is required for PERCENT coupon");
		}
		if (type == PriceCouponType.FIXED && dto.amount() == null) {
			throw new IllegalArgumentException("amount is required for FIXED coupon");
		}
		return new PriceCoupon(type, dto.ratePercent(), dto.amount(), dto.minSpend(), dto.cap());
	}

	private static ShippingCoupon toDomain(ShippingCouponDto dto) {
		Objects.requireNonNull(dto, "shippingCoupon");
		return new ShippingCoupon(dto.shippingDiscount(), dto.cap());
	}

	private static PromoCombinationResultDto toDto(PromoCombinationResult result) {
		PriceCouponDto priceCoupon = result.priceCoupon() == null ? null : toDto(result.priceCoupon());
		ShippingCouponDto shippingCoupon = result.shippingCoupon() == null ? null : toDto(result.shippingCoupon());
		PriceDiscountResultDto priceResult = toDto(result.priceResult());
		ShippingDiscountResultDto shippingResult = toDto(result.shippingResult());
		return new PromoCombinationResultDto(
			priceCoupon,
			shippingCoupon,
			priceResult,
			shippingResult,
			result.totalDiscount(),
			result.finalAmount()
		);
	}

	private static PriceCouponDto toDto(PriceCoupon coupon) {
		return new PriceCouponDto(
			coupon.type(),
			coupon.ratePercent(),
			coupon.amount(),
			coupon.minSpend(),
			coupon.cap()
		);
	}

	private static ShippingCouponDto toDto(ShippingCoupon coupon) {
		return new ShippingCouponDto(coupon.shippingDiscount(), coupon.cap());
	}

	private static PriceDiscountResultDto toDto(PriceDiscountResult result) {
		return new PriceDiscountResultDto(
			result.applied(),
			result.discount(),
			result.finalAmount(),
			result.reason()
		);
	}

	private static ShippingDiscountResultDto toDto(ShippingDiscountResult result) {
		return new ShippingDiscountResultDto(
			result.applied(),
			result.discount(),
			result.remainingShippingFee(),
			result.reason()
		);
	}

	public record PromoRequest(
		long subtotal,
		long shippingFee,
		List<PriceCoupon> priceCoupons,
		List<ShippingCoupon> shippingCoupons
	) {}
}
