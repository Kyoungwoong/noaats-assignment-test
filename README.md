# NoahATS Pre-Task: Promo Value Calculator (MVP)

이 프로젝트는 **복합 할인 정책(적용 순서/최소금액/상한/중복)** 때문에 발생하는
“실질 결제액 판단의 어려움”을 해결하기 위한 계산기 MVP다.

---

## 문서 안내
- 문제 정의/기획: `Problem.md`
- 테스트 시나리오: `TESTCASES.md`
- UI 검증 체크리스트: `UI_CHECKLIST.md`
- 개선 과정/프롬프트 로그 링크: `PROCESS.md`
- AI 활용 기록: `AI_COLLABORATION.md`
- 에이전트 규칙: `AGENTS.md` / `.agents/skills/*/SKILL.md`

---

## 실행 방법

### Backend
```bash
cd backend
./gradlew test
```

### Frontend
```bash
cd frontend
pnpm install
pnpm dev
```

### Docker (compose)
```bash
docker compose up --build
```
접속:
- Frontend: http://localhost:3000
- Backend: http://localhost:8080

## 환경 요구사항
- JDK 17+
- Node.js 18+
- pnpm 9+
- Docker (선택)

---

## Swagger (OpenAPI)
백엔드 실행 후 브라우저에서 확인한다.
- 기본 경로: `/swagger-ui/index.html`

---

## JaCoCo 커버리지 리포트 확인
```bash
cd backend
./gradlew test jacocoTestReport
```
리포트 경로: `backend/build/reports/jacoco/test/html/index.html`

---

## API 요약
- `POST /api/promo/calculate`: 쿠폰 조합 계산
- `POST /api/ev/calculate`: EV 계산
- `POST /api/auth/register`: 회원가입
- `POST /api/auth/login`: 로그인
- `GET /api/history`: 계산 히스토리 조회

---

## 정책/가정 (MVP 고정)
- Apply order: 가격 쿠폰 -> 배송 쿠폰
- minSpend base: 상품 정가 합계(배송비 제외)
- Rounding: 원 단위 버림(floor)
- Clamping: `min(raw, base, cap)`
- cap 미설정: 무제한(∞)
- Stack rules: 가격쿠폰 1장 + 배송쿠폰 1장

---

## 샘플 시나리오 (Expected Outputs)
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

---

## 구조 메모
- 계산 엔진: `backend/src/main/java/com/noaats/backend/promo`
- API 컨트롤러: `backend/src/main/java/com/noaats/backend/controller`
- 테스트: `backend/src/test/java/com/noaats/backend`
- 프론트 UI: `frontend/src/app/page.tsx`

---

## AGENTS / Skills / Rules
- 전역 규칙: `AGENTS.md`
- 프로젝트 스킬: `.agents/skills/*/SKILL.md`
- 운영 규칙: `codex-operations` 스킬
- Git 워크플로우: `git-workflow` 스킬
