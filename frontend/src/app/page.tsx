"use client";

import { useEffect, useState } from "react";
import styles from "./page.module.css";

type HealthResponse = {
  status: string;
  time: string;
};

const API_URL = "http://localhost:8080/api/health";

export default function Home() {
  const [data, setData] = useState<HealthResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    let active = true;
    const load = async () => {
      try {
        const res = await fetch(API_URL);
        if (!res.ok) {
          throw new Error(`HTTP ${res.status}`);
        }
        const json = (await res.json()) as HealthResponse;
        if (active) {
          setData(json);
        }
      } catch (err) {
        if (active) {
          setError(err instanceof Error ? err.message : "Unknown error");
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    };

    load();
    return () => {
      active = false;
    };
  }, []);

  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <h1 className={styles.title}>Backend Health Check</h1>
        <p className={styles.desc}>
          프론트에서 백엔드 API를 호출해 정상 연동 여부를 확인합니다.
        </p>

        <div className={styles.card}>
          <div className={styles.row}>
            <span className={styles.label}>API</span>
            <span className={styles.value}>{API_URL}</span>
          </div>

          {loading && <div className={styles.status}>Loading...</div>}
          {!loading && error && (
            <div className={styles.error}>Error: {error}</div>
          )}
          {!loading && !error && data && (
            <div className={styles.status}>
              status: {data.status} / time: {data.time}
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
