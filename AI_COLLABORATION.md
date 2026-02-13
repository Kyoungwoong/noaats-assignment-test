# AI Collaboration Notes

이 문서는 **AI를 어떤 컨텍스트로 사용했고**, **어떻게 검증/수정했는지**를 기록한다.
또한 `AGENTS.md`의 프로토콜과 **Skill 기반 작업 방식**을 요약해,
작업 흐름과 책임 경계를 명확하게 보여준다.

---

## 1. AI에게 제공한 컨텍스트
- 문제 정의 및 정책 고정: `Problem.md`
- 진행 계획: `plan.md`
- 평가 기준/규칙: `AGENTS.md`, `.agents/skills/*/SKILL.md`
- 테스트/시나리오: `TESTCASES.md`

---

## 2. AGENTS.md 기반 작업 규칙 요약
- 승인 기반 단계 진행(Plan → Build → Verify → Package)
- 계산 로직은 고위험으로 취급하고 정책/정의 고정
- 결정/정책은 `plan.md`에 기록하여 컨텍스트 지속성 확보
- Git workflow와 프롬프트 로그는 **기능 단위**로 관리

---

## 2.1 Rule 관리 방식 (요약)
본 프로젝트의 Rule은 `AGENTS.md`를 최상위 기준으로 관리하고,
세부 규칙은 `.agents/skills/*/SKILL.md`에 분리하여 적용하고 있다.
작업 시에는 **Rule 우선순위(AGENTS → Skills → 코드베이스)**를 준수하며,
결정 사항은 `plan.md`에 기록해 지속적으로 갱신하고 있다.

### Rule 요약 (간단)
- 승인 기반 단계 진행(Plan → Build → Verify → Package)
- 계산 로직은 고위험으로 취급, 정책/정의 고정
- Git workflow 준수(이슈 → 브랜치 → 커밋/푸시 → PR)
- 프롬프트 로그는 기능 단위로, PR 직전에 기록

---

## 3. Skill 개념 및 사용 방식
**Skill**은 특정 작업 규칙/기술 컨텍스트를 강제하는 문서다.  
AI는 작업 시 해당 Skill 내용을 우선 적용하며, 관련 파일 링크는 아래에 둔다.

### 3.1 주요 Skill 목록 (링크 포함)
- [codex-operations](./.agents/skills/codex-operations/SKILL.md): 탐색/수정/명령 실행 규칙
- [git-workflow](./.agents/skills/git-conventions/SKILL.md): Issue/Branch/Commit/PR 규칙
- [springboot-tdd](./.agents/skills/springboot-tdd/SKILL.md): Spring Boot TDD 가이드
- [tdd-loop](./.agents/skills/tdd-loop/SKILL.md): RED → GREEN → REFACTOR
- [tdd-workflow](./.agents/skills/tdd-workflow/SKILL.md): TDD 스크립트 작성 규칙
- [backend-patterns](./.agents/skills/backend-patterns/SKILL.md): 백엔드 구조/설계 패턴
- [java-coding-standards](./.agents/skills/java-coding-standards/SKILL.md): Java 코드 규칙
- [jpa-patterns](./.agents/skills/jpa-patterns/SKILL.md): JPA 패턴
- [assessment-context-skill](./.agents/skills/context-skill/SKILL.md): 과제 컨텍스트 정리
- [assessment-problem-solving-skill](./.agents/skills/problem-solving-skill/SKILL.md): 문제 해결 방식
- [assessment-checklists](./.agents/skills/assessment-checklists/SKILL.md): 평가 기준 체크리스트

---

## 4. AI 협업 작업 흐름 (요약)
1. **Problem 정의**: `Problem.md` 분석 및 목표/정책 고정  
2. **Plan 모드**: 계획 수립 및 `plan.md` 기록  
3. **GitHub MCP 기반 이슈 생성**  
4. **브랜치 생성**: `noaats/<type>/<issue>-<desc>`  
5. **개발 진행**: 관련 Skill 규칙 준수  
6. **git add/commit/push**  
7. **PR 생성** + `prompts/` 로그(기능 단위, PR 직전 기록)  
8. **리뷰 코멘트**: PR에 리뷰 코멘트 작성  
9. **머지 후 다음 작업 진행**

---

## 5. AI 생성 결과의 검증 방식
- 단위 테스트로 계산 결과 검증 (`./gradlew test`)
- 시나리오(S1~S3) 및 Top3 정렬 규칙의 기대값 고정
- 정책/사유/부족 금액 등 UI 표기의 결과 일치성 확인

---

## 6. 수정/보완 내역
- Top3 정렬 기준을 정책 문서와 일치하도록 수정.
- minSpend 미달 시 부족 금액 표시 및 이유 노출 강화.
- 공통 응답/에러 포맷 정리 및 GlobalExceptionHandler 적용.
- 결제수단 제한 조건 미노출 문제를 UI 비활성/경고로 개선.

---

## 7. AI 협업에서 발견한 문제점과 대응
- 문서와 구현이 어긋나는 경우 발생 → 정책 문서 기준으로 로직/테스트 동기화.
- UI 표기 누락 발견 → 결과 영역에 기준/사유 노출 추가.
- 커버리지 미달 → 미테스트 패키지 중심으로 보강.

---

## 8. 대표 수정 사례
1) Top3 정렬 기준
   - AI 초기 결과가 정렬 기준(결제액 → 할인액 → 입력 순서)을 일부 누락.
   - 정책 문서에 맞춰 Comparator를 수정하고 테스트로 고정.

2) minSpend 미달 처리
   - AI 결과에서 실패 사유/부족 금액이 누락된 부분을 발견.
   - API 응답 및 UI 결과에 사유/부족 금액을 노출하도록 수정.

---

## 9. 프롬프트 로그
기능별 프롬프트 기록은 아래 링크에서 확인한다.
- [가격 쿠폰 계산 엔진](./prompts/price-coupon-engine.md)
- [배송 쿠폰 + Top3 UI](./prompts/feature-shipping-top3-ui.md)
- [Top3 정렬 정책 맞춤](./prompts/feat-top3-sort-policy.md)
- [실질 할인율 2종 표시](./prompts/feat-discount-rates.md)
- [결과 영역 정책 블록](./prompts/feat-result-policy-block.md)
- [minSpend 부족 금액 표시](./prompts/feat-minspend-shortfall.md)
- [추천 사유 표시](./prompts/feat-recommendation-reasons.md)
- [결제수단 제한 처리](./prompts/feat-payment-method-guard.md)
- [쿠폰 조건 검증 강화](./prompts/feat-coupon-conditions.md)
- [공통 응답/에러 포맷 리팩터](./prompts/refactor-common-response-error.md)
- [인증/히스토리 기능](./prompts/feat-auth-history.md)
- [테스트 커버리지 보강](./prompts/feat-test-coverage.md)
- [Swagger/OpenAPI 적용](./prompts/feat-swagger-openapi.md)
- [UI/UX 대시보드 구성](./prompts/feat-ui-ux-dashboard.md)
- [비교 화면(A/B) 추가](./prompts/feat-compare-view.md)
- [결과 카드 상세 표시](./prompts/feat-ui-result-details.md)
- [EV 계산기](./prompts/feat-ev-calculator.md)
- [DTO 분리 리팩터](./prompts/refactor-split-dtos-maintainability.md)
- [Validation/Hooks/API 리팩터](./prompts/refactor-validation-hooks-api.md)
- [Logback 비동기 롤링 로그](./prompts/chore-logback-async-rolling.md)
