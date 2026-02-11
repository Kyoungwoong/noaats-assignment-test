import type { EvRequest, EvResponse } from "../types/evApi";
import { API_BASE } from "./apiBase";

const API_URL = `${API_BASE}/ev/calculate`;

type ApiEnvelope<T> = {
  success: boolean;
  data: T;
  meta?: Record<string, unknown>;
};

export const calculateEv = async (input: EvRequest): Promise<EvResponse> => {
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

  const json = (await res.json()) as ApiEnvelope<EvResponse>;
  return json.data;
};

export { API_URL as EV_API_URL };
