package com.noaats.backend.promo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class PromoCalculator {
	private PromoCalculator() {}

	public static List<PromoCombinationResult> recommendTop3(
		long subtotal,
		long shippingFee,
		List<CartItem> items,
		PaymentMethod paymentMethod,
		java.time.Instant now,
		List<PriceCoupon> priceCoupons,
		List<ShippingCoupon> shippingCoupons
	) {
		PromoCalculationContext context = new PromoCalculationContext(
			items == null ? List.of() : items.stream().map(CartItem::category).collect(Collectors.toList()),
			paymentMethod,
			now
		);
		List<PromoCombination> combinations = generateCombinations(
			subtotal,
			shippingFee,
			priceCoupons,
			shippingCoupons,
			context
		);
		combinations.sort(Comparator
			.comparingLong(PromoCombination::finalAmount)
			.thenComparing(Comparator.comparingLong(PromoCombination::totalDiscount).reversed())
			.thenComparing(Comparator.comparingDouble(PromoCombination::discountRateByTotal).reversed())
			.thenComparingInt(PromoCombination::orderIndex)
		);

		int limit = Math.min(3, combinations.size());
		List<PromoCombinationResult> results = new ArrayList<>(limit);
		for (int i = 0; i < limit; i++) {
			PromoCombination combo = combinations.get(i);
			String reason = i == 0 ? "결제액 최소" : (i == 1 ? "총할인액 우선" : "할인율 우선");
			results.add(new PromoCombinationResult(
				combo.priceCoupon(),
				combo.shippingCoupon(),
				combo.priceResult(),
				combo.shippingResult(),
				combo.totalDiscount(),
				combo.finalAmount(),
				reason,
				combo.discountRateBySubtotal(),
				combo.discountRateByTotal()
			));
		}
		return Collections.unmodifiableList(results);
	}

	private static List<PromoCombination> generateCombinations(
		long subtotal,
		long shippingFee,
		List<PriceCoupon> priceCoupons,
		List<ShippingCoupon> shippingCoupons,
		PromoCalculationContext context
	) {
		List<PriceCoupon> priceOptions = new ArrayList<>();
		priceOptions.add(null);
		if (priceCoupons != null) {
			priceOptions.addAll(priceCoupons);
		}

		List<ShippingCoupon> shippingOptions = new ArrayList<>();
		shippingOptions.add(null);
		if (shippingCoupons != null) {
			shippingOptions.addAll(shippingCoupons);
		}

		List<PromoCombination> combinations = new ArrayList<>();
		int index = 0;
		for (PriceCoupon priceCoupon : priceOptions) {
			PriceDiscountResult priceResult = priceCoupon == null
				? new PriceDiscountResult(false, 0L, subtotal + shippingFee, null)
				: PriceCouponCalculator.calculate(subtotal, shippingFee, priceCoupon, context);

			for (ShippingCoupon shippingCoupon : shippingOptions) {
				ShippingDiscountResult shippingResult = shippingCoupon == null
					? new ShippingDiscountResult(false, 0L, shippingFee, null)
					: ShippingCouponCalculator.calculate(shippingFee, shippingCoupon, context);

				long totalDiscount = priceResult.discount() + shippingResult.discount();
				long finalAmount = (subtotal + shippingFee) - totalDiscount;
				double subtotalBase = subtotal > 0 ? subtotal : 0.0;
				double totalBase = (subtotal + shippingFee) > 0 ? (subtotal + shippingFee) : 0.0;
				double rateBySubtotal = subtotalBase > 0 ? (double) totalDiscount / subtotalBase : 0.0;
				double rateByTotal = totalBase > 0 ? (double) totalDiscount / totalBase : 0.0;
				combinations.add(new PromoCombination(
					priceCoupon,
					shippingCoupon,
					priceResult,
					shippingResult,
					totalDiscount,
					finalAmount,
					rateBySubtotal,
					rateByTotal,
					index++
				));
			}
		}

		return combinations;
	}

	private record PromoCombination(
		PriceCoupon priceCoupon,
		ShippingCoupon shippingCoupon,
		PriceDiscountResult priceResult,
		ShippingDiscountResult shippingResult,
		long totalDiscount,
		long finalAmount,
		double discountRateBySubtotal,
		double discountRateByTotal,
		int orderIndex
	) {}
}
