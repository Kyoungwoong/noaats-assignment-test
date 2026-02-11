# AI Collaboration Notes

## Scope of AI Contribution
- 가격쿠폰(%/정액) 계산 엔진 설계 및 구현
- S1/S3 시나리오 기반 단위 테스트 작성
- 배송쿠폰 + Top3 추천 로직 설계 및 구현
- S2/Top3 테스트 추가
- UI 입력/결과 화면 구현
- 계획/정책 문서화 (`plan.md`, `README.md`, `TESTCASES.md`)

## Prompt / Hypothesis / Verification
- Hypothesis: 고정 정책(적용 순서, minSpend base, floor, clamping)으로 가격/배송쿠폰과 Top3 조합을 계산하면 S1~S3 결과가 재현된다.
- Verification:
  - `./gradlew test` 실행 예정
  - S1/S2/S3 및 Top3 테스트 결과 확인 예정

## Corrections / Adjustments
- S3 시나리오를 minSpend 미달 케이스로 테스트에 추가하여 정책 위반 시 동작을 고정.
- tdd-workflow의 docs/tdd 스크립트는 사용자 요청으로 생략하고 TDD 루프만 수행.

## Open Items
- 없음 (v1 범위 내 완료 목표)
