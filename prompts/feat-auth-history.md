# Feature: auth + history dashboard

## User request
- 간단 로그인(JWT)
- 마이페이지에 계산 히스토리 대시보드
- 카드 클릭 시 입력/결과 상세 표시

## Assistant actions
- Added JWT auth with register/login and security config
- Added H2 persistence with User and PromoHistory entities
- Added history list/detail endpoints and save-on-calc behavior
- Added frontend auth UI and MyPage history dashboard/detail
- Added unit tests for auth/history and fixed controller test
- Ran backend tests

## Notes
- plan.md is gitignored and not committed
