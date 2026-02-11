import type { PaymentMethod, PriceCouponType } from "./promoCommon";

export type CartItemInput = {
  id: string;
  name: string;
  price: string;
  quantity: string;
  category: string;
};

export type PriceCouponInput = {
  id: string;
  type: PriceCouponType;
  ratePercent: string;
  amount: string;
  minSpend: string;
  cap: string;
  excludedCategories: string;
  allowedPaymentMethods: PaymentMethod[];
  validFrom: string;
  validTo: string;
};

export type ShippingCouponInput = {
  id: string;
  shippingDiscount: string;
  cap: string;
  excludedCategories: string;
  allowedPaymentMethods: PaymentMethod[];
  validFrom: string;
  validTo: string;
};
