import type { PromoResponse } from "../types/promoApi";
import type { PriceCoupon, ShippingCoupon } from "../types/promoApi";

const API_URL = "http://localhost:8080/api/promo/calculate";

type ApiResponse<T> = {
  success: boolean;
  data: T;
  meta?: Record<string, unknown>;
};

export const calculatePromo = async (input: {
  subtotal: number;
  shippingFee: number;
  priceCoupons: PriceCoupon[];
  shippingCoupons: ShippingCoupon[];
}): Promise<PromoResponse> => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(input),
  });

  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`);
  }

  const json = (await res.json()) as ApiResponse<PromoResponse>;
  return json.data;
};

export { API_URL };
