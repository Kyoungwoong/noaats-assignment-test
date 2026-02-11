import { API_BASE } from "./apiBase";

type ApiEnvelope<T> = {
  success: boolean;
  data: T;
  meta?: Record<string, unknown>;
};

type AuthResponse = {
  accessToken: string;
};

const authRequest = async (path: string, username: string, password: string): Promise<AuthResponse> => {
  const res = await fetch(`${API_BASE}/auth/${path}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  });

  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`);
  }

  const json = (await res.json()) as ApiEnvelope<AuthResponse>;
  return json.data;
};

export const registerUser = async (username: string, password: string) => authRequest("register", username, password);
export const loginUser = async (username: string, password: string) => authRequest("login", username, password);
