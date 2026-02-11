import type { PaymentMethod, PriceCouponType } from "./promoCommon";

export type CartItem = {
  name: string;
  price: number;
  quantity: number;
  category: string;
};

export type PriceCoupon = {
  type: PriceCouponType;
  ratePercent?: number | null;
  amount?: number | null;
  minSpend?: number | null;
  cap?: number | null;
  excludedCategories?: string[] | null;
  allowedPaymentMethods?: PaymentMethod[] | null;
  validFrom?: string | null;
  validTo?: string | null;
};

export type ShippingCoupon = {
  shippingDiscount?: number | null;
  cap?: number | null;
  excludedCategories?: string[] | null;
  allowedPaymentMethods?: PaymentMethod[] | null;
  validFrom?: string | null;
  validTo?: string | null;
};

export type PromoRequest = {
  subtotal: number;
  items: CartItem[];
  shippingFee: number;
  paymentMethod: PaymentMethod;
  priceCoupons: PriceCoupon[];
  shippingCoupons: ShippingCoupon[];
};

export type PriceDiscountResult = {
  applied: boolean;
  discount: number;
  finalAmount: number;
  reason: string | null;
  shortfallAmount: number;
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
  reason: string;
  discountRateBySubtotal: number;
  discountRateByTotal: number;
};

export type PromoResponse = {
  top3: PromoCombinationResult[];
};
