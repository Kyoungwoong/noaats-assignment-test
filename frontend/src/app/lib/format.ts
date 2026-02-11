import type { PriceCoupon } from "../types/promoApi";

export const formatCurrency = (value?: number | null) => {
  if (value === null || value === undefined) {
    return "-";
  }
  return `${value.toLocaleString()}원`;
};

export const formatPriceCoupon = (coupon: PriceCoupon) => {
  if (coupon.type === "PERCENT") {
    return coupon.ratePercent !== null && coupon.ratePercent !== undefined
      ? `${coupon.ratePercent}% 할인`
      : "-";
  }
  return coupon.amount !== null && coupon.amount !== undefined
    ? `${coupon.amount.toLocaleString()}원 할인`
    : "-";
};
