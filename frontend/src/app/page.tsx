"use client";

import { useEffect, useState } from "react";
import styles from "./page.module.css";
import { calculatePromo, API_URL } from "./lib/promoApi";
import { calculateEv } from "./lib/evApi";
import { loginUser, registerUser } from "./lib/authApi";
import { fetchHistoryDetail, fetchHistoryList } from "./lib/historyApi";
import { formatCurrency, formatPriceCoupon } from "./lib/format";
import { usePromoForm } from "./hooks/usePromoForm";
import type { PromoCombinationResult, PromoResponse } from "./types/promoApi";
import type { EvRequest, EvResponse, EvScenario } from "./types/evApi";
import type { PaymentMethod } from "./types/promoCommon";
import type { HistoryDetail, HistorySummary } from "./lib/historyApi";

const PAYMENT_METHODS: PaymentMethod[] = ["CARD", "BANK", "KAKAO"];
const EV_REWARD_TYPES = ["CASH", "POINT", "PERCENT"] as const;
const TABS = [
  { id: "CALC", label: "계산" },
  { id: "EV", label: "EV·포인트" },
  { id: "POLICY", label: "정책" },
] as const;
type TabId = (typeof TABS)[number]["id"] | "MY";

export default function Home() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [results, setResults] = useState<PromoCombinationResult[]>([]);
  const [selectedCompareIndexes, setSelectedCompareIndexes] = useState<number[]>([]);
  const [activeTab, setActiveTab] = useState<TabId>("CALC");
  const [authToken, setAuthToken] = useState<string | null>(null);
  const [authView, setAuthView] = useState<"login" | "register">("login");
  const [authUsername, setAuthUsername] = useState("");
  const [authPassword, setAuthPassword] = useState("");
  const [authMessage, setAuthMessage] = useState<string | null>(null);
  const [historyList, setHistoryList] = useState<HistorySummary[]>([]);
  const [selectedHistory, setSelectedHistory] = useState<HistoryDetail | null>(null);
  const [evBaseAmount, setEvBaseAmount] = useState("59000");
  const [evScenarios, setEvScenarios] = useState<EvScenario[]>([
    { label: "당첨 5%", probability: 0.05, rewardType: "POINT", rewardValue: 1000 },
    { label: "당첨 1%", probability: 0.01, rewardType: "CASH", rewardValue: 5000 },
  ]);
  const [evResult, setEvResult] = useState<EvResponse | null>(null);

  const {
    items,
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
  } = usePromoForm();

  useEffect(() => {
    const stored = window.localStorage.getItem("noaats_token");
    if (stored) {
      setAuthToken(stored);
    }
  }, []);

  useEffect(() => {
    if (activeTab === "MY" && authToken) {
      fetchHistoryList(authToken)
        .then(setHistoryList)
        .catch(() => setHistoryList([]));
    }
  }, [activeTab, authToken]);

  const handleCalculate = async () => {
    setLoading(true);
    setError(null);
    try {
      const json = (await calculatePromo({
        subtotal: subtotalValue,
        items: normalizedItems,
        shippingFee: Number(shippingFee) || 0,
        paymentMethod,
        priceCoupons: normalizedPriceCoupons,
        shippingCoupons: normalizedShippingCoupons,
      }, authToken)) as PromoResponse;
      setSelectedCompareIndexes([]);
      setResults(json.top3 ?? []);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  };

  const handleEvCalculate = async () => {
    setLoading(true);
    setError(null);
    try {
      const req: EvRequest = {
        baseAmount: Number(evBaseAmount) || 0,
        scenarios: evScenarios,
      };
      const result = await calculateEv(req);
      setEvResult(result);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  };

  const addEvScenario = () => {
    setEvScenarios((prev) => [
      ...prev,
      { label: "신규 시나리오", probability: 0.0, rewardType: "CASH", rewardValue: 0 },
    ]);
  };

  const updateEvScenario = (index: number, patch: Partial<EvScenario>) => {
    setEvScenarios((prev) => prev.map((item, i) => (i === index ? { ...item, ...patch } : item)));
  };

  const removeEvScenario = (index: number) => {
    setEvScenarios((prev) => prev.filter((_, i) => i !== index));
  };

  const toggleCompare = (index: number) => {
    setSelectedCompareIndexes((prev) => {
      if (prev.includes(index)) {
        return prev.filter((value) => value !== index);
      }
      if (prev.length < 2) {
        return [...prev, index];
      }
      return [prev[1], index];
    });
  };

  const compareResults = selectedCompareIndexes
    .map((index) => results[index])
    .filter(Boolean);
  const bestResult = results[0] ?? null;
  const visibleTabs = TABS;
  const isAllowedByPayment = (methods: PaymentMethod[]) =>
    methods.length === 0 || methods.includes(paymentMethod);

  const handleLogin = async (mode: "login" | "register") => {
    setAuthMessage(null);
    try {
      const result =
        mode === "register"
          ? await registerUser(authUsername, authPassword)
          : await loginUser(authUsername, authPassword);
      setAuthToken(result.accessToken);
      window.localStorage.setItem("noaats_token", result.accessToken);
      setAuthMessage(mode === "register" ? "회원가입 완료" : "로그인 완료");
      setActiveTab("MY");
    } catch (err) {
      setAuthMessage(err instanceof Error ? err.message : "로그인 실패");
    }
  };

  const handleLogout = () => {
    window.localStorage.removeItem("noaats_token");
    setAuthToken(null);
    setActiveTab("CALC");
    setHistoryList([]);
    setSelectedHistory(null);
  };

  const handleSelectHistory = async (item: HistorySummary) => {
    if (!authToken) {
      return;
    }
    try {
      const detail = await fetchHistoryDetail(authToken, item.id);
      setSelectedHistory(detail);
    } catch {
      setSelectedHistory(null);
    }
  };

  return (
    <div className={styles.page}>
      <header className={styles.navBar}>
        <div className={styles.navLogo}>
          <span className={styles.kicker}>Promo Value</span>
          <strong className={styles.brand}>Calculator</strong>
        </div>
        <nav className={styles.navTabs}>
          {visibleTabs.map((tab) => (
            <button
              key={tab.id}
              type="button"
              className={`${styles.tabButton} ${activeTab === tab.id ? styles.tabButtonActive : ""}`}
              aria-pressed={activeTab === tab.id}
              onClick={() => setActiveTab(tab.id)}
            >
              {tab.label}
            </button>
          ))}
        </nav>
        <div className={styles.navActions}>
          {!authToken ? (
            <>
              <button
                type="button"
                className={styles.navButtonGhost}
                onClick={() => {
                  setAuthView("login");
                  setActiveTab("MY");
                }}
              >
                로그인
              </button>
            <button
              type="button"
              className={styles.navButtonGhost}
              onClick={() => {
                setAuthView("register");
                setActiveTab("MY");
              }}
            >
              회원가입
            </button>
            </>
          ) : (
            <>
              <button type="button" className={styles.navButtonGhost} onClick={handleLogout}>
                로그아웃
              </button>
              <button type="button" className={styles.navButton} onClick={() => setActiveTab("MY")}>
                마이페이지
              </button>
            </>
          )}
        </div>
      </header>

      <main className={styles.main}>
        {activeTab === "POLICY" && (
          <section className={styles.panel}>
            <div className={styles.sectionHeader}>
              <h2 className={styles.sectionTitle}>정책 요약</h2>
            </div>
            <ul className={styles.policyList}>
              <li>적용 순서: 가격 쿠폰 → 배송 쿠폰</li>
              <li>minSpend 기준: 상품 정가 합계</li>
              <li>반올림: 원 단위 버림</li>
              <li>클램프: min(raw, base, cap)</li>
              <li>중복 규칙: 가격 1장 + 배송 1장</li>
            </ul>
          </section>
        )}

        {activeTab === "CALC" && (
          <>
            <section className={styles.overviewPanel}>
              <div>
                <p className={styles.kicker}>Promo Value Calculator</p>
                <h1 className={styles.title}>실질 할인 가치 계산기</h1>
                <p className={styles.subtitle}>
                  입력값을 기준으로 쿠폰 조합을 계산하고, 결제액 최소 Top3를 추천합니다.
                </p>
              </div>
              <div className={styles.kpiGrid}>
                <div className={styles.kpiCard}>
                  <span className={styles.kpiLabel}>상품 합계</span>
                  <strong className={styles.kpiValue}>{subtotalValue.toLocaleString()}원</strong>
                </div>
                <div className={styles.kpiCard}>
                  <span className={styles.kpiLabel}>배송비</span>
                  <strong className={styles.kpiValue}>{Number(shippingFee || 0).toLocaleString()}원</strong>
                </div>
                <div className={styles.kpiCard}>
                  <span className={styles.kpiLabel}>최저 결제액</span>
                  <strong className={styles.kpiValue}>
                    {bestResult ? `${bestResult.finalAmount.toLocaleString()}원` : "-"}
                  </strong>
                </div>
              </div>
            </section>

            <div className={styles.dashboardGrid}>
              <div className={styles.dashboardCol}>
        <section className={styles.panel}>
          <div className={styles.sectionHeader}>
            <h2 className={styles.sectionTitle}>장바구니 아이템</h2>
            <button type="button" className={styles.ghostButton} onClick={addItem}>
              + 아이템 추가
            </button>
          </div>
          <div className={styles.stack}>
            {items.map((item) => (
              <div key={item.id} className={styles.couponCard}>
                <div className={styles.rowBetween}>
                  <div className={styles.row}>
                    <label className={styles.fieldSmall}>
                      <span>이름</span>
                      <input
                        value={item.name}
                        onChange={(event) => updateItem(item.id, { name: event.target.value })}
                      />
                    </label>
                    <label className={styles.fieldSmall}>
                      <span>카테고리</span>
                      <input
                        value={item.category}
                        onChange={(event) => updateItem(item.id, { category: event.target.value })}
                      />
                    </label>
                  </div>
                  <button
                    type="button"
                    className={styles.removeButton}
                    onClick={() => removeItem(item.id)}
                  >
                    삭제
                  </button>
                </div>
                <div className={styles.row}>
                  <label className={styles.fieldSmall}>
                    <span>가격</span>
                    <input
                      type="number"
                      min="0"
                      value={item.price}
                      onChange={(event) => updateItem(item.id, { price: event.target.value })}
                    />
                  </label>
                  <label className={styles.fieldSmall}>
                    <span>수량</span>
                    <input
                      type="number"
                      min="1"
                      value={item.quantity}
                      onChange={(event) => updateItem(item.id, { quantity: event.target.value })}
                    />
                  </label>
                </div>
              </div>
            ))}
          </div>
          <div className={styles.row}>
            <label className={styles.fieldSmall}>
              <span>결제수단</span>
              <select value={paymentMethod} onChange={(event) => setPaymentMethod(event.target.value as PaymentMethod)}>
                {PAYMENT_METHODS.map((method) => (
                  <option key={method} value={method}>
                    {method}
                  </option>
                ))}
              </select>
            </label>
            <label className={styles.fieldSmall}>
              <span>배송비</span>
              <input
                type="number"
                min="0"
                value={shippingFee}
                onChange={(event) => setShippingFee(event.target.value)}
              />
            </label>
            <div className={styles.fieldSmall}>
              <span>상품 합계</span>
              <strong>{subtotalValue.toLocaleString()}원</strong>
            </div>
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
            {priceCoupons.map((coupon) => {
              const isAllowed = isAllowedByPayment(coupon.allowedPaymentMethods);
              return (
              <div
                key={coupon.id}
                className={`${styles.couponCard} ${!isAllowed ? styles.couponCardDisabled : ""}`}
                aria-disabled={!isAllowed}
              >
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
                          onChange={(event) => updatePriceCoupon(coupon.id, { ratePercent: event.target.value })}
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
                {!isAllowed && (
                  <p className={styles.couponWarning} title="선택한 결제수단에서 사용할 수 없습니다.">
                    결제수단 제한: {paymentMethod} 불가
                  </p>
                )}
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
                <div className={styles.row}>
                  <label className={styles.fieldSmall}>
                    <span>제외 카테고리(,)</span>
                    <input
                      value={coupon.excludedCategories}
                      onChange={(event) =>
                        updatePriceCoupon(coupon.id, { excludedCategories: event.target.value })
                      }
                    />
                  </label>
                  <label className={styles.fieldSmall}>
                    <span>결제수단 제한</span>
                    <div className={styles.chipGroup}>
                      {PAYMENT_METHODS.map((method) => {
                        const active = coupon.allowedPaymentMethods.includes(method);
                        return (
                          <label key={method} className={`${styles.chip} ${active ? styles.chipActive : ""}`}>
                            <input
                              type="checkbox"
                              checked={active}
                              onChange={() => {
                                const next = active
                                  ? coupon.allowedPaymentMethods.filter((item) => item !== method)
                                  : [...coupon.allowedPaymentMethods, method];
                                updatePriceCoupon(coupon.id, { allowedPaymentMethods: next });
                              }}
                            />
                            <span>{method}</span>
                          </label>
                        );
                      })}
                    </div>
                  </label>
                </div>
                <div className={styles.row}>
                  <label className={styles.fieldSmall}>
                    <span>validFrom</span>
                    <input
                      type="datetime-local"
                      value={coupon.validFrom}
                      onChange={(event) => updatePriceCoupon(coupon.id, { validFrom: event.target.value })}
                    />
                  </label>
                  <label className={styles.fieldSmall}>
                    <span>validTo</span>
                    <input
                      type="datetime-local"
                      value={coupon.validTo}
                      onChange={(event) => updatePriceCoupon(coupon.id, { validTo: event.target.value })}
                    />
                  </label>
                </div>
              </div>
            );
            })}
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
            {shippingCoupons.map((coupon) => {
              const isAllowed = isAllowedByPayment(coupon.allowedPaymentMethods);
              return (
              <div
                key={coupon.id}
                className={`${styles.couponCard} ${!isAllowed ? styles.couponCardDisabled : ""}`}
                aria-disabled={!isAllowed}
              >
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
                {!isAllowed && (
                  <p className={styles.couponWarning} title="선택한 결제수단에서 사용할 수 없습니다.">
                    결제수단 제한: {paymentMethod} 불가
                  </p>
                )}
                <div className={styles.row}>
                  <label className={styles.fieldSmall}>
                    <span>제외 카테고리(,)</span>
                    <input
                      value={coupon.excludedCategories}
                      onChange={(event) =>
                        updateShippingCoupon(coupon.id, { excludedCategories: event.target.value })
                      }
                    />
                  </label>
                  <label className={styles.fieldSmall}>
                    <span>결제수단 제한</span>
                    <div className={styles.chipGroup}>
                      {PAYMENT_METHODS.map((method) => {
                        const active = coupon.allowedPaymentMethods.includes(method);
                        return (
                          <label key={method} className={`${styles.chip} ${active ? styles.chipActive : ""}`}>
                            <input
                              type="checkbox"
                              checked={active}
                              onChange={() => {
                                const next = active
                                  ? coupon.allowedPaymentMethods.filter((item) => item !== method)
                                  : [...coupon.allowedPaymentMethods, method];
                                updateShippingCoupon(coupon.id, { allowedPaymentMethods: next });
                              }}
                            />
                            <span>{method}</span>
                          </label>
                        );
                      })}
                    </div>
                  </label>
                </div>
                <div className={styles.row}>
                  <label className={styles.fieldSmall}>
                    <span>validFrom</span>
                    <input
                      type="datetime-local"
                      value={coupon.validFrom}
                      onChange={(event) => updateShippingCoupon(coupon.id, { validFrom: event.target.value })}
                    />
                  </label>
                  <label className={styles.fieldSmall}>
                    <span>validTo</span>
                    <input
                      type="datetime-local"
                      value={coupon.validTo}
                      onChange={(event) => updateShippingCoupon(coupon.id, { validTo: event.target.value })}
                    />
                  </label>
                </div>
              </div>
            );
            })}
          </div>
        </section>
              </div>
              <div className={styles.dashboardCol}>

        <section className={styles.panel}>
          <div className={styles.sectionHeader}>
            <h2 className={styles.sectionTitle}>Top3 추천 결과</h2>
            <button type="button" className={styles.primaryButton} onClick={handleCalculate}>
              {loading ? "계산 중..." : "계산하기"}
            </button>
          </div>
          <div className={styles.resultPolicy}>
            <ul className={styles.policyList}>
              <li>적용 순서: 가격 쿠폰 → 배송 쿠폰</li>
              <li>minSpend 기준: 상품 정가 합계</li>
              <li>반올림: 원 단위 버림</li>
              <li>클램프: min(raw, base, cap)</li>
              <li>중복 규칙: 가격 1장 + 배송 1장</li>
            </ul>
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
                  <button
                    type="button"
                    className={`${styles.compareButton} ${
                      selectedCompareIndexes.includes(index) ? styles.compareButtonActive : ""
                    }`}
                    onClick={() => toggleCompare(index)}
                  >
                    {selectedCompareIndexes.includes(index) ? "비교 해제" : "비교 선택"}
                  </button>
                </div>
                <div className={styles.resultBody}>
                  <div>
                    <p className={styles.label}>총 할인</p>
                    <p className={styles.value}>{result.totalDiscount.toLocaleString()}원</p>
                  </div>
                  <div>
                    <p className={styles.label}>추천 사유</p>
                    <p className={styles.valueSmall}>{result.reason}</p>
                  </div>
                  <div>
                    <p className={styles.label}>실질 할인율(상품)</p>
                    <p className={styles.valueSmall}>
                      {(result.discountRateBySubtotal * 100).toFixed(2)}%
                    </p>
                  </div>
                  <div>
                    <p className={styles.label}>실질 할인율(총액)</p>
                    <p className={styles.valueSmall}>
                      {(result.discountRateByTotal * 100).toFixed(2)}%
                    </p>
                  </div>
                  <div>
                    <p className={styles.label}>가격쿠폰</p>
                    <p className={styles.valueSmall}>
                      {result.priceCoupon ? formatPriceCoupon(result.priceCoupon) : "미사용"}
                    </p>
                    {result.priceResult.applied && result.priceResult.discount > 0 && (
                      <p className={styles.valueSmall}>
                        실제 할인액: {formatCurrency(result.priceResult.discount)}
                      </p>
                    )}
                  </div>
                  {result.priceResult.reason === "PAYMENT_METHOD_NOT_ALLOWED" && (
                    <div>
                      <p className={styles.label}>불가 사유</p>
                      <p className={styles.valueSmall}>결제수단 제한</p>
                    </div>
                  )}
                  {result.priceResult.reason === "MIN_SPEND_NOT_MET" && result.priceResult.shortfallAmount > 0 && (
                    <div>
                      <p className={styles.label}>부족 금액</p>
                      <p className={styles.valueSmall}>
                        {formatCurrency(result.priceResult.shortfallAmount)}
                      </p>
                      <p className={styles.valueSmall}>적용 실패</p>
                    </div>
                  )}
                  <div>
                    <p className={styles.label}>배송쿠폰</p>
                    <p className={styles.valueSmall}>
                      {result.shippingCoupon ? formatCurrency(result.shippingCoupon.shippingDiscount) : "미사용"}
                    </p>
                    {result.shippingResult.applied && result.shippingResult.discount > 0 && (
                      <p className={styles.valueSmall}>
                        실제 할인액: {formatCurrency(result.shippingResult.discount)}
                      </p>
                    )}
                  </div>
                  {result.shippingResult.reason === "PAYMENT_METHOD_NOT_ALLOWED" && (
                    <div>
                      <p className={styles.label}>불가 사유</p>
                      <p className={styles.valueSmall}>결제수단 제한</p>
                    </div>
                  )}
                </div>
              </article>
            ))}
          </div>
          <div className={styles.comparePanel}>
            <h3 className={styles.compareTitle}>A/B 비교</h3>
            {compareResults.length < 2 ? (
              <p className={styles.empty}>비교할 결과 2개를 선택하세요.</p>
            ) : (
              <div className={styles.compareTable}>
                <div className={styles.compareRow}>
                  <div className={styles.compareLabel}></div>
                  <div className={styles.compareCell}>
                    A (#{selectedCompareIndexes[0] + 1})
                  </div>
                  <div className={styles.compareCell}>
                    B (#{selectedCompareIndexes[1] + 1})
                  </div>
                </div>
                <div className={styles.compareRow}>
                  <div className={styles.compareLabel}>최종 결제액</div>
                  <div className={styles.compareCell}>
                    {compareResults[0].finalAmount.toLocaleString()}원
                  </div>
                  <div className={styles.compareCell}>
                    {compareResults[1].finalAmount.toLocaleString()}원
                  </div>
                </div>
                <div className={styles.compareRow}>
                  <div className={styles.compareLabel}>총 할인</div>
                  <div className={styles.compareCell}>
                    {compareResults[0].totalDiscount.toLocaleString()}원
                  </div>
                  <div className={styles.compareCell}>
                    {compareResults[1].totalDiscount.toLocaleString()}원
                  </div>
                </div>
                <div className={styles.compareRow}>
                  <div className={styles.compareLabel}>실질 할인율(상품)</div>
                  <div className={styles.compareCell}>
                    {(compareResults[0].discountRateBySubtotal * 100).toFixed(2)}%
                  </div>
                  <div className={styles.compareCell}>
                    {(compareResults[1].discountRateBySubtotal * 100).toFixed(2)}%
                  </div>
                </div>
                <div className={styles.compareRow}>
                  <div className={styles.compareLabel}>실질 할인율(총액)</div>
                  <div className={styles.compareCell}>
                    {(compareResults[0].discountRateByTotal * 100).toFixed(2)}%
                  </div>
                  <div className={styles.compareCell}>
                    {(compareResults[1].discountRateByTotal * 100).toFixed(2)}%
                  </div>
                </div>
              </div>
            )}
          </div>
        </section>
              </div>
            </div>
          </>
        )}

        {activeTab === "EV" && (
        <section className={`${styles.panel} ${styles.evPanel}`}>
          <div className={styles.sectionHeader}>
            <h2 className={styles.sectionTitle}>확률형 EV 계산</h2>
            <button type="button" className={styles.primaryButton} onClick={handleEvCalculate}>
              {loading ? "계산 중..." : "EV 계산하기"}
            </button>
          </div>
          <div className={styles.row}>
            <label className={styles.fieldSmall}>
              <span>기준 금액</span>
              <input
                type="number"
                min="0"
                value={evBaseAmount}
                onChange={(event) => setEvBaseAmount(event.target.value)}
              />
            </label>
            <button type="button" className={styles.ghostButton} onClick={addEvScenario}>
              + 시나리오 추가
            </button>
          </div>
          <div className={styles.stack}>
            {evScenarios.map((scenario, index) => (
              <div key={`${scenario.label}-${index}`} className={styles.couponCard}>
                <div className={styles.rowBetween}>
                  <div className={styles.row}>
                    <label className={styles.fieldSmall}>
                      <span>라벨</span>
                      <input
                        value={scenario.label}
                        onChange={(event) => updateEvScenario(index, { label: event.target.value })}
                      />
                    </label>
                    <label className={styles.fieldSmall}>
                      <span>확률(0~1)</span>
                      <input
                        type="number"
                        step="0.01"
                        min="0"
                        max="1"
                        value={scenario.probability}
                        onChange={(event) =>
                          updateEvScenario(index, { probability: Number(event.target.value) || 0 })
                        }
                      />
                    </label>
                  </div>
                  <button
                    type="button"
                    className={styles.removeButton}
                    onClick={() => removeEvScenario(index)}
                  >
                    삭제
                  </button>
                </div>
                <div className={styles.row}>
                  <label className={styles.fieldSmall}>
                    <span>보상 유형</span>
                    <select
                      value={scenario.rewardType}
                      onChange={(event) =>
                        updateEvScenario(index, { rewardType: event.target.value as EvScenario["rewardType"] })
                      }
                    >
                      {EV_REWARD_TYPES.map((type) => (
                        <option key={type} value={type}>
                          {type}
                        </option>
                      ))}
                    </select>
                  </label>
                  <label className={styles.fieldSmall}>
                    <span>보상 값</span>
                    <input
                      type="number"
                      min="0"
                      value={scenario.rewardValue}
                      onChange={(event) =>
                        updateEvScenario(index, { rewardValue: Number(event.target.value) || 0 })
                      }
                    />
                  </label>
                </div>
              </div>
            ))}
          </div>
          {evResult && (
            <div className={styles.card}>
              <p className={styles.label}>기대값</p>
              <p className={styles.value}>{evResult.expectedValue.toLocaleString()}원</p>
              <p className={styles.label}>기대 할인율</p>
              <p className={styles.valueSmall}>{(evResult.expectedDiscountRate * 100).toFixed(2)}%</p>
            </div>
          )}
        </section>
        )}

        {activeTab === "MY" && (
          <section className={styles.panel}>
            <div className={styles.sectionHeader}>
              <h2 className={styles.sectionTitle}>마이페이지</h2>
              {authToken && (
                <button type="button" className={styles.ghostButton} onClick={handleLogout}>
                  로그아웃
                </button>
              )}
            </div>
            {!authToken ? (
              <div className={styles.authPanel}>
                <p className={styles.label}>
                  {authView === "register" ? "회원가입 페이지" : "로그인 페이지"}
                </p>
                <label className={styles.field}>
                  <span>아이디</span>
                  <input value={authUsername} onChange={(event) => setAuthUsername(event.target.value)} />
                </label>
                <label className={styles.field}>
                  <span>비밀번호</span>
                  <input
                    type="password"
                    value={authPassword}
                    onChange={(event) => setAuthPassword(event.target.value)}
                  />
                </label>
                <div className={styles.row}>
                  {authView === "register" ? (
                    <>
                      <button
                        type="button"
                        className={styles.primaryButton}
                        onClick={() => handleLogin("register")}
                      >
                        회원가입
                      </button>
                      <button
                        type="button"
                        className={styles.ghostButton}
                        onClick={() => setAuthView("login")}
                      >
                        로그인으로
                      </button>
                    </>
                  ) : (
                    <>
                      <button type="button" className={styles.primaryButton} onClick={() => handleLogin("login")}>
                        로그인
                      </button>
                      <button
                        type="button"
                        className={styles.ghostButton}
                        onClick={() => setAuthView("register")}
                      >
                        회원가입으로
                      </button>
                    </>
                  )}
                </div>
                {authMessage && <p className={styles.valueSmall}>{authMessage}</p>}
              </div>
            ) : (
              <div className={styles.historyGrid}>
                <div className={styles.historyList}>
                  {historyList.length === 0 && <p className={styles.empty}>히스토리가 없습니다.</p>}
                  {historyList.map((item) => (
                    <button
                      type="button"
                      key={item.id}
                      className={styles.historyCard}
                      onClick={() => handleSelectHistory(item)}
                    >
                      <span className={styles.rank}>#{item.id}</span>
                      <strong>{item.finalAmount.toLocaleString()}원</strong>
                      <span className={styles.valueSmall}>
                        할인 {item.totalDiscount.toLocaleString()}원 · 배송 {item.shippingFee.toLocaleString()}원
                      </span>
                    </button>
                  ))}
                </div>
                <div className={styles.historyDetail}>
                  {selectedHistory ? (
                    <>
                      <h3 className={styles.compareTitle}>히스토리 상세</h3>
                      <div className={styles.historySection}>
                        <p className={styles.label}>요청 입력</p>
                        <pre className={styles.historyJson}>
                          {JSON.stringify(selectedHistory.request, null, 2)}
                        </pre>
                      </div>
                      <div className={styles.historySection}>
                        <p className={styles.label}>계산 결과</p>
                        <pre className={styles.historyJson}>
                          {JSON.stringify(selectedHistory.response, null, 2)}
                        </pre>
                      </div>
                    </>
                  ) : (
                    <p className={styles.empty}>상세를 선택하세요.</p>
                  )}
                </div>
              </div>
            )}
          </section>
        )}
      </main>

      <footer className={styles.footer}>
        <p>API: {API_URL}</p>
      </footer>
    </div>
  );
}
