import type { PriceCouponType } from "./promoCommon";

export type PriceCouponInput = {
  id: string;
  type: PriceCouponType;
  ratePercent: string;
  amount: string;
  minSpend: string;
  cap: string;
};

export type ShippingCouponInput = {
  id: string;
  shippingDiscount: string;
  cap: string;
};
