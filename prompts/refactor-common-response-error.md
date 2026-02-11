# Prompt Log: common-response-error

## User
오케이 완료했어. 이제 마지막 리팩토링이야. 공통 에러 양식, 공통 응답 양식, globalExceptionHandler를 넣고 싶어. 이거 분석해봐.

## Assistant
현황 탐색 후 공통 응답/에러 포맷 및 예외 처리 계획 제시.

## User
공통 응답 포맷과 공통 에러 포맷 두가지가 있는거야. 그것을 적용해야해. 당연히 제네릭으로 작성해야겠지? 현재까지 적용된 모든 API에 넣어주고, Exception들 마다 나눠서 handler 적용해주고, Exception 만들어주고, ErrorCode enum으로 만들어줘.

## Assistant
공통 포맷+예외 핸들러+ErrorCode 적용 계획 확정.

## User
Implement the plan.
