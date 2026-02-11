import type { PriceCouponType } from "./promoCommon";

export type PriceCoupon = {
  type: PriceCouponType;
  ratePercent?: number | null;
  amount?: number | null;
  minSpend?: number | null;
  cap?: number | null;
};

export type ShippingCoupon = {
  shippingDiscount?: number | null;
  cap?: number | null;
};

export type PriceDiscountResult = {
  applied: boolean;
  discount: number;
  finalAmount: number;
  reason: string | null;
};

export type ShippingDiscountResult = {
  applied: boolean;
  discount: number;
  remainingShippingFee: number;
  reason: string | null;
};

export type PromoCombinationResult = {
  priceCoupon: PriceCoupon | null;
  shippingCoupon: ShippingCoupon | null;
  priceResult: PriceDiscountResult;
  shippingResult: ShippingDiscountResult;
  totalDiscount: number;
  finalAmount: number;
};

export type PromoResponse = {
  top3: PromoCombinationResult[];
};
