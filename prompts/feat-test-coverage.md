# Feature: Backend JaCoCo coverage 80%+

## User request
- 백엔드 테스트 커버리지를 80% 이상으로 올리고 JaCoCo 도입

## Assistant actions
- Added JaCoCo plugin and coverage verification (LINE >= 0.80)
- Added/expanded tests across promo/auth/history/controller/exception/api/ev
- Removed stray .DS_Store and added to .gitignore
- Ran `./gradlew test jacocoTestReport jacocoTestCoverageVerification`

## Notes
- Coverage verification passed
