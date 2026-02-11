import type { PromoRequest, PromoResponse } from "../types/promoApi";
import { API_BASE } from "./apiBase";

const API_URL = `${API_BASE}/promo/calculate`;

type ApiEnvelope<T> = {
  success: boolean;
  data: T;
  meta?: Record<string, unknown>;
};

export const calculatePromo = async (input: PromoRequest, token?: string | null): Promise<PromoResponse> => {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  const res = await fetch(API_URL, {
    method: "POST",
    headers,
    body: JSON.stringify(input),
  });

  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`);
  }

  const json = (await res.json()) as ApiEnvelope<PromoResponse>;
  return json.data;
};

export { API_URL };
