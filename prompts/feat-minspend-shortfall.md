# Feature: minSpend shortfall display

## User request
- minSpend 미달 시 부족 금액 표시
- Top3 결과 카드에 표시
- 제네릭/공통 규칙 유지, 테스트 실행

## Assistant actions
- Added shortfallAmount to PriceDiscountResult and DTO mapping
- Calculated shortfall when minSpend not met in PriceCouponCalculator
- Added unit test for shortfall amount
- Updated frontend types and Top3 UI to show shortfall amount
- Ran ./gradlew test

## Notes
- plan.md is gitignored and not committed
