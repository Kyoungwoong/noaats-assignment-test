import { useMemo, useState } from "react";

import type { PriceCoupon, ShippingCoupon } from "../types/promoApi";
import type { PriceCouponInput, ShippingCouponInput } from "../types/promoForm";
import type { PriceCouponType } from "../types/promoCommon";

const emptyPriceCoupon = (): PriceCouponInput => ({
  id: crypto.randomUUID(),
  type: "PERCENT",
  ratePercent: "",
  amount: "",
  minSpend: "",
  cap: "",
});

const emptyShippingCoupon = (): ShippingCouponInput => ({
  id: crypto.randomUUID(),
  shippingDiscount: "",
  cap: "",
});

const toNullableNumber = (value: string): number | null => {
  if (value.trim() === "") {
    return null;
  }
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : null;
};

export const usePromoForm = () => {
  const [subtotal, setSubtotal] = useState("59000");
  const [shippingFee, setShippingFee] = useState("3000");
  const [priceCoupons, setPriceCoupons] = useState<PriceCouponInput[]>([
    {
      id: crypto.randomUUID(),
      type: "PERCENT",
      ratePercent: "20",
      amount: "",
      minSpend: "50000",
      cap: "8000",
    },
    {
      id: crypto.randomUUID(),
      type: "FIXED",
      ratePercent: "",
      amount: "7000",
      minSpend: "40000",
      cap: "",
    },
  ]);
  const [shippingCoupons, setShippingCoupons] = useState<ShippingCouponInput[]>([
    {
      id: crypto.randomUUID(),
      shippingDiscount: "3000",
      cap: "3000",
    },
  ]);

  const subtotalValue = toNullableNumber(subtotal) ?? 0;
  const shippingValue = toNullableNumber(shippingFee) ?? 0;

  const normalizedPriceCoupons = useMemo(() => {
    return priceCoupons
      .map((coupon): PriceCoupon => ({
        type: coupon.type,
        ratePercent: coupon.type === "PERCENT" ? toNullableNumber(coupon.ratePercent) : null,
        amount: coupon.type === "FIXED" ? toNullableNumber(coupon.amount) : null,
        minSpend: toNullableNumber(coupon.minSpend),
        cap: toNullableNumber(coupon.cap),
      }))
      .filter((coupon) => {
        if (coupon.type === "PERCENT") {
          return coupon.ratePercent !== null;
        }
        return coupon.amount !== null;
      });
  }, [priceCoupons]);

  const normalizedShippingCoupons = useMemo(() => {
    return shippingCoupons
      .map((coupon): ShippingCoupon => ({
        shippingDiscount: toNullableNumber(coupon.shippingDiscount),
        cap: toNullableNumber(coupon.cap),
      }))
      .filter((coupon) => coupon.shippingDiscount !== null);
  }, [shippingCoupons]);

  const addPriceCoupon = () => {
    setPriceCoupons((prev) => [...prev, emptyPriceCoupon()]);
  };

  const addShippingCoupon = () => {
    setShippingCoupons((prev) => [...prev, emptyShippingCoupon()]);
  };

  const removePriceCoupon = (id: string) => {
    setPriceCoupons((prev) => prev.filter((coupon) => coupon.id !== id));
  };

  const removeShippingCoupon = (id: string) => {
    setShippingCoupons((prev) => prev.filter((coupon) => coupon.id !== id));
  };

  const updatePriceCoupon = (id: string, patch: Partial<PriceCouponInput>) => {
    setPriceCoupons((prev) => prev.map((item) => (item.id === id ? { ...item, ...patch } : item)));
  };

  const updateShippingCoupon = (id: string, patch: Partial<ShippingCouponInput>) => {
    setShippingCoupons((prev) => prev.map((item) => (item.id === id ? { ...item, ...patch } : item)));
  };

  return {
    subtotal,
    setSubtotal,
    shippingFee,
    setShippingFee,
    priceCoupons,
    shippingCoupons,
    subtotalValue,
    shippingValue,
    normalizedPriceCoupons,
    normalizedShippingCoupons,
    addPriceCoupon,
    addShippingCoupon,
    removePriceCoupon,
    removeShippingCoupon,
    updatePriceCoupon,
    updateShippingCoupon,
  } as const;
};

export type { PriceCouponInput, ShippingCouponInput, PriceCouponType };
