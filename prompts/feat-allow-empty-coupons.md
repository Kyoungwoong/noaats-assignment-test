# Feature: Allow empty coupon lists

## User request
- 쿠폰 없이도 계산 가능하도록 요청 DTO 제약 완화

## Assistant actions
- Removed @NotEmpty from price/shipping coupon lists in PromoRequestDto
- Updated tests to cover empty coupon lists
- Ran `./gradlew test`

## Notes
- Behavior unchanged; only validation relaxed
