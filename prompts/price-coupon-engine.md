# Price Coupon Engine Prompt Log

## Context
- Feature: 가격쿠폰(%/정액) 계산 엔진 + S1/S3 테스트
- Policy: minSpend base=상품 정가 합계, floor, clamping=min(raw, base, cap)

## User Prompts (Summary)
- PROBLEM.md 확인 후 간단한 기능 구현 요청
- S1 시나리오 기준 계산 엔진 + 테스트 구현 승인
- Git workflow 강제(이슈 → 브랜치 → 커밋 → 푸시 → PR), 커밋은 기능 단위로 분리
- prompts/ 폴더에 기능별 프롬프트 기록 요청
- plan.md는 git 관리 제외(ignored)

## AI Responses (Summary)
- 가격쿠폰 계산 엔진/테스트 구현 및 검증 진행
- 문서화(README, AI_COLLABORATION, TESTCASES) 추가
- Git workflow 스킬 확인 및 브랜치/커밋 분리 진행 준비

## Outputs
- Engine: `backend/src/main/java/com/noaats/backend/promo/*`
- Tests: `backend/src/test/java/com/noaats/backend/promo/PriceCouponCalculatorTest.java`
- Docs: `README.md`, `AI_COLLABORATION.md`, `TESTCASES.md`
