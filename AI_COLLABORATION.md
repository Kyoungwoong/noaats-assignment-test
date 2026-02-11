# AI Collaboration Notes

## Scope of AI Contribution
- 가격쿠폰(%/정액) 계산 엔진 설계 및 구현
- S1/S3 시나리오 기반 단위 테스트 작성
- 계획/정책 문서화 (`plan.md`, `README.md`)

## Prompt / Hypothesis / Verification
- Hypothesis: 고정 정책(적용 순서, minSpend base, floor, clamping)에 따라 계산 엔진을 만들면 S1/S3 결과가 재현된다.
- Verification:
  - `./gradlew test` 실행
  - S1/S3 테스트 모두 통과 확인

## Corrections / Adjustments
- S3 시나리오를 minSpend 미달 케이스로 테스트에 추가하여 정책 위반 시 동작을 고정.

## Open Items
- 배송쿠폰/추천 Top3/프론트 UI는 v1 범위 밖으로 유지.
