# 개발 과정 기록 (Process Log)

이 문서는 **문제 해결 접근, 개선 과정, 우선순위 조정**을 기록한다.
기능별 작업 내역은 `prompts/`의 로그로 연결한다.

---

## 1. 개발 순서 개요
1) 문제 정의 및 정책 고정  
2) 계산 엔진 및 핵심 로직 구현  
3) 시나리오 기반 테스트 확장  
4) UI/UX 고도화 및 정책/사유 표시  
5) 인증/히스토리/공통 응답 구조 등 확장  
6) 테스트 커버리지 및 JaCoCo 도입  

---

## 2. 개선 과정 및 변경 사항

### 2.1 계산 엔진/정책 고정
- 핵심 정책(적용 순서, minSpend 기준, floor, clamping)을 고정하고 모든 테스트/결과에 반영.
- Top3 추천 정렬 기준을 정책 문서와 일치하도록 수정.

관련 프롬프트:
- [가격 쿠폰 계산 엔진](./prompts/price-coupon-engine.md)
- [배송 쿠폰 + Top3 UI](./prompts/feature-shipping-top3-ui.md)
- [Top3 정렬 정책 맞춤](./prompts/feat-top3-sort-policy.md)

### 2.2 입력/결과 UX 개선
- 실질 할인율 2종 표기, 정책/기준 표시, 부족 금액 표시.
- 결과 사유/추천 사유 표시 강화.

관련 프롬프트:
- [실질 할인율 2종 표시](./prompts/feat-discount-rates.md)
- [결과 영역 정책 블록](./prompts/feat-result-policy-block.md)
- [minSpend 부족 금액 표시](./prompts/feat-minspend-shortfall.md)
- [추천 사유 표시](./prompts/feat-recommendation-reasons.md)
- [결과 카드 상세 표시](./prompts/feat-ui-result-details.md)
- [UI/UX 대시보드 구성](./prompts/feat-ui-ux-dashboard.md)
- [비교 화면(A/B) 추가](./prompts/feat-compare-view.md)

### 2.3 조건/제약 확장
- 결제수단 제한 조건 및 UI 차단 처리.
- 쿠폰 조건 검증 로직 강화.

관련 프롬프트:
- [결제수단 제한 처리](./prompts/feat-payment-method-guard.md)
- [쿠폰 조건 검증 강화](./prompts/feat-coupon-conditions.md)

### 2.4 공통 응답/예외 처리 구조화
- 공통 응답 포맷 및 GlobalExceptionHandler 적용.
- ErrorCode enum 및 표준 에러 응답 구조 정립.

관련 프롬프트:
- [공통 응답/에러 포맷 리팩터](./prompts/refactor-common-response-error.md)

### 2.5 인증/히스토리 기능 추가
- 로그인/회원가입 및 히스토리 저장/조회 흐름 구성.
- 마이페이지 히스토리 대시보드 UX 확장.

관련 프롬프트:
- [인증/히스토리 기능](./prompts/feat-auth-history.md)

### 2.6 문서/테스트 강화
- 테스트 커버리지 80% 이상 확보 및 JaCoCo 도입.
- Swagger/OpenAPI 문서 보강.

관련 프롬프트:
- [테스트 커버리지 보강](./prompts/feat-test-coverage.md)
- [Swagger/OpenAPI 적용](./prompts/feat-swagger-openapi.md)

---

## 3. 막혔던 부분과 해결
- CORS 오류: `WebConfig`에 허용 Origin 적용으로 해결.
- 회원가입 400 오류: DTO/Validation 수정 및 API 요청 포맷 정리로 해결.
- 커버리지 미달: 미테스트 패키지(예외/DTO/컨트롤러) 보강으로 해결.

---

## 4. 우선순위 조정/시간 배분
1) 계산 엔진 정확성 →  
2) 테스트로 정책 고정 →  
3) UI/UX 노출 기준 강화 →  
4) 인증/히스토리 →  
5) 커버리지/문서화

---

## 5. 작업 로그(선택)
- `prompts/` 폴더에 기능별 기록 유지
