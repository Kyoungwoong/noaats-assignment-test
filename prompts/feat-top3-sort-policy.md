# Feature: Top3 sorting policy alignment

## User request
- Top3 정렬 기준을 정책(결제액 최소 → 총할인액 → 입력 순서 유지)에 맞게 수정

## Assistant actions
- Updated PromoCalculator sort order and reason label
- Updated PromoCalculatorTest expectations
- Ran `./gradlew test`

## Notes
- No other behavior changes
