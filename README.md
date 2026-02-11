# NoahATS Pre-Task: Promo Value Calculator (MVP)

## How to Run (Backend Tests)
```bash
cd backend
./gradlew test
```

## Policies / Assumptions (MVP)
- Apply order: 가격 쿠폰 -> 배송 쿠폰
- minSpend base: 상품 정가 합계(배송비 제외)
- Rounding: 원 단위 버림(floor)
- Clamping: `min(raw, base, cap)`
- cap 미설정: 무제한(∞)
- Stack rules: 가격쿠폰 1장 + 배송쿠폰 1장

## Sample Scenarios (Expected Outputs)
### S1: %쿠폰 vs 정액쿠폰 비교
- Input:
  - subtotal=59,000, shipping=3,000
  - C1: 20%(cap 8,000, min 50,000)
  - C2: 7,000(min 40,000)
- Expected:
  - C1 final=54,000, discount=8,000
  - C2 final=55,000, discount=7,000

### S2: 배송쿠폰 효과 확인
- Input:
  - subtotal=19,900, shipping=3,000
  - S: shippingDiscount=3,000, cap=3,000
- Expected:
  - shipping fee=0, final=19,900, shipping discount=3,000

### S3: 최소금액 미달
- Input:
  - subtotal=28,000, shipping=3,000
  - C: 6,000(min 30,000)
- Expected:
  - applied=false, discount=0, final=31,000, reason=MIN_SPEND_NOT_MET

## Project Notes
- 계산 엔진은 `backend/src/main/java/com/noaats/backend/promo` 아래에 있다.
- 테스트는 `backend/src/test/java/com/noaats/backend/promo`에 있다.
- 프론트 UI는 `frontend/src/app/page.tsx`에서 API(`POST /api/promo/calculate`)를 호출한다.
