import { useMemo, useState } from "react";

import type { CartItem, PriceCoupon, ShippingCoupon } from "../types/promoApi";
import type {
  CartItemInput,
  PriceCouponInput,
  ShippingCouponInput,
} from "../types/promoForm";
import type { PaymentMethod, PriceCouponType } from "../types/promoCommon";

const emptyItem = (): CartItemInput => ({
  id: crypto.randomUUID(),
  name: "Item",
  price: "10000",
  quantity: "1",
  category: "SHOES",
});

const emptyPriceCoupon = (): PriceCouponInput => ({
  id: crypto.randomUUID(),
  type: "PERCENT",
  ratePercent: "",
  amount: "",
  minSpend: "",
  cap: "",
  excludedCategories: "",
  allowedPaymentMethods: [],
  validFrom: "",
  validTo: "",
});

const emptyShippingCoupon = (): ShippingCouponInput => ({
  id: crypto.randomUUID(),
  shippingDiscount: "",
  cap: "",
  excludedCategories: "",
  allowedPaymentMethods: [],
  validFrom: "",
  validTo: "",
});

const toNullableNumber = (value: string): number | null => {
  if (value.trim() === "") {
    return null;
  }
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : null;
};

const parseList = (value: string): string[] =>
  value
    .split(",")
    .map((entry) => entry.trim())
    .filter((entry) => entry.length > 0);

export const usePromoForm = () => {
  const [items, setItems] = useState<CartItemInput[]>([emptyItem()]);
  const [shippingFee, setShippingFee] = useState("3000");
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>("CARD");
  const [priceCoupons, setPriceCoupons] = useState<PriceCouponInput[]>([
    {
      id: crypto.randomUUID(),
      type: "PERCENT",
      ratePercent: "20",
      amount: "",
      minSpend: "50000",
      cap: "8000",
      excludedCategories: "",
      allowedPaymentMethods: ["CARD"],
      validFrom: "",
      validTo: "",
    },
    {
      id: crypto.randomUUID(),
      type: "FIXED",
      ratePercent: "",
      amount: "7000",
      minSpend: "40000",
      cap: "",
      excludedCategories: "",
      allowedPaymentMethods: ["CARD"],
      validFrom: "",
      validTo: "",
    },
  ]);
  const [shippingCoupons, setShippingCoupons] = useState<ShippingCouponInput[]>([
    {
      id: crypto.randomUUID(),
      shippingDiscount: "3000",
      cap: "3000",
      excludedCategories: "",
      allowedPaymentMethods: ["CARD"],
      validFrom: "",
      validTo: "",
    },
  ]);

  const subtotalValue = useMemo(() => {
    return items.reduce((sum, item) => {
      const price = toNullableNumber(item.price) ?? 0;
      const quantity = toNullableNumber(item.quantity) ?? 0;
      return sum + price * quantity;
    }, 0);
  }, [items]);

  const shippingValue = toNullableNumber(shippingFee) ?? 0;

  const normalizedItems = useMemo(() => {
    return items
      .map((item): CartItem => ({
        name: item.name.trim() || "Item",
        price: toNullableNumber(item.price) ?? 0,
        quantity: Math.max(1, Math.trunc(toNullableNumber(item.quantity) ?? 1)),
        category: item.category.trim() || "MISC",
      }))
      .filter((item) => item.price > 0 && item.quantity > 0);
  }, [items]);

  const normalizedPriceCoupons = useMemo(() => {
    return priceCoupons
      .map((coupon): PriceCoupon => ({
        type: coupon.type,
        ratePercent: coupon.type === "PERCENT" ? toNullableNumber(coupon.ratePercent) : null,
        amount: coupon.type === "FIXED" ? toNullableNumber(coupon.amount) : null,
        minSpend: toNullableNumber(coupon.minSpend),
        cap: toNullableNumber(coupon.cap),
        excludedCategories: parseList(coupon.excludedCategories),
        allowedPaymentMethods: coupon.allowedPaymentMethods,
        validFrom: coupon.validFrom.trim() || null,
        validTo: coupon.validTo.trim() || null,
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
        excludedCategories: parseList(coupon.excludedCategories),
        allowedPaymentMethods: coupon.allowedPaymentMethods,
        validFrom: coupon.validFrom.trim() || null,
        validTo: coupon.validTo.trim() || null,
      }))
      .filter((coupon) => coupon.shippingDiscount !== null);
  }, [shippingCoupons]);

  const addItem = () => {
    setItems((prev) => [...prev, emptyItem()]);
  };

  const removeItem = (id: string) => {
    setItems((prev) => prev.filter((item) => item.id !== id));
  };

  const updateItem = (id: string, patch: Partial<CartItemInput>) => {
    setItems((prev) => prev.map((item) => (item.id === id ? { ...item, ...patch } : item)));
  };

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
    items,
    setItems,
    addItem,
    removeItem,
    updateItem,
    subtotalValue,
    shippingFee,
    setShippingFee,
    paymentMethod,
    setPaymentMethod,
    priceCoupons,
    shippingCoupons,
    normalizedItems,
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

export type { CartItemInput, PriceCouponInput, ShippingCouponInput, PriceCouponType, PaymentMethod };
