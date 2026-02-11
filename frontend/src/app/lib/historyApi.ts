import { API_BASE } from "./apiBase";

type ApiEnvelope<T> = {
  success: boolean;
  data: T;
  meta?: Record<string, unknown>;
};

export type HistorySummary = {
  id: number;
  createdAt: string;
  subtotal: number;
  shippingFee: number;
  finalAmount: number;
  totalDiscount: number;
};

export type HistoryDetail = HistorySummary & {
  request: Record<string, unknown>;
  response: Record<string, unknown>;
};

const authHeaders = (token: string) => ({
  Authorization: `Bearer ${token}`,
});

export const fetchHistoryList = async (token: string): Promise<HistorySummary[]> => {
  const res = await fetch(`${API_BASE}/history`, {
    headers: authHeaders(token),
  });
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`);
  }
  const json = (await res.json()) as ApiEnvelope<HistorySummary[]>;
  return json.data;
};

export const fetchHistoryDetail = async (token: string, id: number): Promise<HistoryDetail> => {
  const res = await fetch(`${API_BASE}/history/${id}`, {
    headers: authHeaders(token),
  });
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`);
  }
  const json = (await res.json()) as ApiEnvelope<HistoryDetail>;
  return json.data;
};
