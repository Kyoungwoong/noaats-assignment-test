# AI Collaboration Notes

이 문서는 **AI를 어떤 컨텍스트로 사용했고**, **어떻게 검증/수정했는지**를 기록한다.

---

## 1. AI에게 제공한 컨텍스트
- 문제 정의 및 정책 고정: `Problem.md`
- 진행 계획: `plan.md`
- 평가 기준/규칙: `AGENTS.md`, `.agents/skills/*/SKILL.md`
- 테스트/시나리오: `TESTCASES.md`

---

## 2. AI 생성 결과의 검증 방식
- 단위 테스트로 계산 결과 검증 (`./gradlew test`)
- 시나리오(S1~S3) 및 Top3 정렬 규칙의 기대값 고정
- 정책/사유/부족 금액 등 UI 표기의 결과 일치성 확인

---

## 3. 수정/보완 내역
- Top3 정렬 기준을 정책 문서와 일치하도록 수정.
- minSpend 미달 시 부족 금액 표시 및 이유 노출 강화.
- 공통 응답/에러 포맷 정리 및 GlobalExceptionHandler 적용.
- 결제수단 제한 조건 미노출 문제를 UI 비활성/경고로 개선.

---

## 4. AI 협업에서 발견한 문제점과 대응
- 문서와 구현이 어긋나는 경우 발생 → 정책 문서 기준으로 로직/테스트 동기화.
- UI 표기 누락 발견 → 결과 영역에 기준/사유 노출 추가.
- 커버리지 미달 → 미테스트 패키지 중심으로 보강.

---

## 5. 대표 수정 사례
1) Top3 정렬 기준
   - AI 초기 결과가 정렬 기준(결제액 → 할인액 → 입력 순서)을 일부 누락.
   - 정책 문서에 맞춰 Comparator를 수정하고 테스트로 고정.

2) minSpend 미달 처리
   - AI 결과에서 실패 사유/부족 금액이 누락된 부분을 발견.
   - API 응답 및 UI 결과에 사유/부족 금액을 노출하도록 수정.

---

## 6. 프롬프트 로그
기능별 프롬프트 기록은 아래 링크에서 확인한다.
- [price-coupon-engine](./prompts/price-coupon-engine.md)
- [feature-shipping-top3-ui](./prompts/feature-shipping-top3-ui.md)
- [feat-top3-sort-policy](./prompts/feat-top3-sort-policy.md)
- [feat-discount-rates](./prompts/feat-discount-rates.md)
- [feat-result-policy-block](./prompts/feat-result-policy-block.md)
- [feat-minspend-shortfall](./prompts/feat-minspend-shortfall.md)
- [feat-recommendation-reasons](./prompts/feat-recommendation-reasons.md)
- [feat-payment-method-guard](./prompts/feat-payment-method-guard.md)
- [feat-coupon-conditions](./prompts/feat-coupon-conditions.md)
- [refactor-common-response-error](./prompts/refactor-common-response-error.md)
- [feat-auth-history](./prompts/feat-auth-history.md)
- [feat-test-coverage](./prompts/feat-test-coverage.md)
- [feat-swagger-openapi](./prompts/feat-swagger-openapi.md)
- [feat-ui-ux-dashboard](./prompts/feat-ui-ux-dashboard.md)
- [feat-compare-view](./prompts/feat-compare-view.md)
- [feat-ev-calculator](./prompts/feat-ev-calculator.md)
- [refactor-split-dtos-maintainability](./prompts/refactor-split-dtos-maintainability.md)
- [refactor-validation-hooks-api](./prompts/refactor-validation-hooks-api.md)
- [chore-logback-async-rolling](./prompts/chore-logback-async-rolling.md)
