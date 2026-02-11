# Prompt Log: shipping-coupon-top3-ui

## User
현재까지 작업한 것을 확인해줘. AGENTS.md와 SKILL들에 있는 내용들을 모두확인하고 PROBLEM.md에 있는 내용을 확인한 후, 어디까지 되었는지 확인해봐.

## Assistant
확인 결과, 현재 상태는 “가격쿠폰 계산 엔진 + S1/S3 테스트”까지 구현 및 문서화된 상태입니다. 배송쿠폰, 추천 Top3, 프론트 UI는 범위 밖으로 남겨둔 상태로 보입니다.

## User
다음 개발 계확을 정하자. plan.md를 정하자는 얘기야.

## Assistant
(다음 범위/프론트/검증 기준 선택 질문)

## User
1. git-workflow가
  진행해야하며, 2. 커밋은 기능단위로 작게. 3. prompts/내부에 기능별로 파일을 나누어서 너와의 내용을
  정리하는 md 파일 작성해야돼. 이거 알고있지?

## Assistant
네. git-workflow 준수, 기능 단위 소커밋, prompts/에 기능별 대화 로그 md 파일 작성 모두 반영합니다.

## User
마찬가지로 Java convention / coding-convention / backend-pattern / tdd skill들 모두 적용하면서 다음 개발 진행하자. 제발 스킬 적용해줘.

## Assistant
요청한 스킬들 적용. 단계 승인 요청.

## User
docs/tdd 스크립트 작성은 안해도 돼. tdd 사이클을 진행해줘. 제발. 이제 진행하자.

## Assistant
tdd-workflow 문서 작성은 생략하고 TDD 루프만 수행하겠다고 확인, 승인 요청.

## User
plan + build 승인

## Assistant
확장 구현 및 테스트, UI까지 완료.

## User
prompt에 내용을 기록하는 것은 매번매번 기록하지 말고, pr 생성까지 다 하고 나서 기능단위로 logging 하는 것으로 수정해줘. 1. 테스트 실행해줘.

## Assistant
테스트 실행 완료. prompts 로그는 PR 직전에 기능 단위로 정리하기로 반영.

## User
아니 prompt 관련 내용은 AGENTS.md에 넣어야 너가 알아듣는거아니야?

## Assistant
AGENTS.md에 규칙 추가 제안.

## User
1번으로 진행해줘. 그리고 그 이후 git add/commit/push, pr 생성까지 해줘.
