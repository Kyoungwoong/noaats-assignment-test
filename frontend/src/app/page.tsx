"use client";

import { useState } from "react";
import styles from "./page.module.css";
import { calculatePromo, API_URL } from "./lib/promoApi";
import { formatCurrency, formatPriceCoupon } from "./lib/format";
import { usePromoForm } from "./hooks/usePromoForm";
import type { PromoCombinationResult, PromoResponse } from "./types/promoApi";

export default function Home() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [results, setResults] = useState<PromoCombinationResult[]>([]);

  const {
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
  } = usePromoForm();

  const handleCalculate = async () => {
    setLoading(true);
    setError(null);
    try {
      const json = (await calculatePromo({
        subtotal: subtotalValue,
        shippingFee: shippingValue,
        priceCoupons: normalizedPriceCoupons,
        shippingCoupons: normalizedShippingCoupons,
      })) as PromoResponse;
      setResults(json.top3 ?? []);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <header className={styles.hero}>
        <div className={styles.heroContent}>
          <p className={styles.kicker}>Promo Value Calculator</p>
          <h1 className={styles.title}>실질 할인 가치 계산기</h1>
          <p className={styles.subtitle}>
            가격쿠폰과 배송쿠폰을 기준 정책으로 계산하고, 결제액 최소 Top3 조합을 추천합니다.
          </p>
        </div>
        <div className={styles.heroCard}>
          <h2 className={styles.sectionTitle}>고정 정책</h2>
          <ul className={styles.policyList}>
            <li>적용 순서: 가격 쿠폰 → 배송 쿠폰</li>
            <li>minSpend 기준: 상품 정가 합계</li>
            <li>반올림: 원 단위 버림</li>
            <li>클램프: min(raw, base, cap)</li>
            <li>중복 규칙: 가격 1장 + 배송 1장</li>
          </ul>
        </div>
      </header>

      <main className={styles.main}>
        <section className={styles.panel}>
          <h2 className={styles.sectionTitle}>장바구니 입력</h2>
          <div className={styles.gridTwo}>
            <label className={styles.field}>
              <span>상품 합계</span>
              <input
                type="number"
                min="0"
                value={subtotal}
                onChange={(event) => setSubtotal(event.target.value)}
                placeholder="예: 59000"
              />
            </label>
            <label className={styles.field}>
              <span>배송비</span>
              <input
                type="number"
                min="0"
                value={shippingFee}
                onChange={(event) => setShippingFee(event.target.value)}
                placeholder="예: 3000"
              />
            </label>
          </div>
        </section>

        <section className={styles.panel}>
          <div className={styles.sectionHeader}>
            <h2 className={styles.sectionTitle}>가격 쿠폰</h2>
            <button type="button" className={styles.ghostButton} onClick={addPriceCoupon}>
              + 추가
            </button>
          </div>
          <div className={styles.stack}>
            {priceCoupons.map((coupon) => (
              <div key={coupon.id} className={styles.couponCard}>
                <div className={styles.rowBetween}>
                  <div className={styles.row}>
                    <label className={styles.fieldSmall}>
                      <span>타입</span>
                      <select
                        value={coupon.type}
                        onChange={(event) =>
                          updatePriceCoupon(coupon.id, { type: event.target.value as typeof coupon.type })
                        }
                      >
                        <option value="PERCENT">% 할인</option>
                        <option value="FIXED">정액 할인</option>
                      </select>
                    </label>
                    {coupon.type === "PERCENT" ? (
                      <label className={styles.fieldSmall}>
                        <span>할인율(%)</span>
                        <input
                          type="number"
                          min="0"
                          value={coupon.ratePercent}
                          onChange={(event) =>
                            updatePriceCoupon(coupon.id, { ratePercent: event.target.value })
                          }
                        />
                      </label>
                    ) : (
                      <label className={styles.fieldSmall}>
                        <span>할인액</span>
                        <input
                          type="number"
                          min="0"
                          value={coupon.amount}
                          onChange={(event) => updatePriceCoupon(coupon.id, { amount: event.target.value })}
                        />
                      </label>
                    )}
                  </div>
                  <button
                    type="button"
                    className={styles.removeButton}
                    onClick={() => removePriceCoupon(coupon.id)}
                  >
                    삭제
                  </button>
                </div>
                <div className={styles.row}>
                  <label className={styles.fieldSmall}>
                    <span>minSpend</span>
                    <input
                      type="number"
                      min="0"
                      value={coupon.minSpend}
                      onChange={(event) => updatePriceCoupon(coupon.id, { minSpend: event.target.value })}
                    />
                  </label>
                  <label className={styles.fieldSmall}>
                    <span>cap</span>
                    <input
                      type="number"
                      min="0"
                      value={coupon.cap}
                      onChange={(event) => updatePriceCoupon(coupon.id, { cap: event.target.value })}
                    />
                  </label>
                </div>
              </div>
            ))}
          </div>
        </section>

        <section className={styles.panel}>
          <div className={styles.sectionHeader}>
            <h2 className={styles.sectionTitle}>배송 쿠폰</h2>
            <button type="button" className={styles.ghostButton} onClick={addShippingCoupon}>
              + 추가
            </button>
          </div>
          <div className={styles.stack}>
            {shippingCoupons.map((coupon) => (
              <div key={coupon.id} className={styles.couponCard}>
                <div className={styles.rowBetween}>
                  <div className={styles.row}>
                    <label className={styles.fieldSmall}>
                      <span>배송 할인액</span>
                      <input
                        type="number"
                        min="0"
                        value={coupon.shippingDiscount}
                        onChange={(event) =>
                          updateShippingCoupon(coupon.id, { shippingDiscount: event.target.value })
                        }
                      />
                    </label>
                    <label className={styles.fieldSmall}>
                      <span>cap</span>
                      <input
                        type="number"
                        min="0"
                        value={coupon.cap}
                        onChange={(event) => updateShippingCoupon(coupon.id, { cap: event.target.value })}
                      />
                    </label>
                  </div>
                  <button
                    type="button"
                    className={styles.removeButton}
                    onClick={() => removeShippingCoupon(coupon.id)}
                  >
                    삭제
                  </button>
                </div>
              </div>
            ))}
          </div>
        </section>

        <section className={styles.panel}>
          <div className={styles.sectionHeader}>
            <h2 className={styles.sectionTitle}>Top3 추천 결과</h2>
            <button type="button" className={styles.primaryButton} onClick={handleCalculate}>
              {loading ? "계산 중..." : "계산하기"}
            </button>
          </div>
          {error && <div className={styles.error}>Error: {error}</div>}
          {!error && results.length === 0 && (
            <p className={styles.empty}>쿠폰 정보를 입력하고 계산을 실행하세요.</p>
          )}
          <div className={styles.resultGrid}>
            {results.map((result, index) => (
              <article key={`result-${index}`} className={styles.resultCard}>
                <div className={styles.resultHeader}>
                  <span className={styles.rank}>#{index + 1}</span>
                  <span className={styles.finalAmount}>
                    최종 결제액 {result.finalAmount.toLocaleString()}원
                  </span>
                </div>
                <div className={styles.resultBody}>
                  <div>
                    <p className={styles.label}>총 할인</p>
                    <p className={styles.value}>{result.totalDiscount.toLocaleString()}원</p>
                  </div>
                  <div>
                    <p className={styles.label}>가격쿠폰</p>
                    <p className={styles.valueSmall}>
                      {result.priceCoupon ? formatPriceCoupon(result.priceCoupon) : "미사용"}
                    </p>
                  </div>
                  <div>
                    <p className={styles.label}>배송쿠폰</p>
                    <p className={styles.valueSmall}>
                      {result.shippingCoupon ? formatCurrency(result.shippingCoupon.shippingDiscount) : "미사용"}
                    </p>
                  </div>
                </div>
              </article>
            ))}
          </div>
        </section>
      </main>

      <footer className={styles.footer}>
        <p>API: {API_URL}</p>
      </footer>
    </div>
  );
}
