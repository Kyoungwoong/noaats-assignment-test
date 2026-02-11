# Prompt Log: logback-async-rolling

## User
오케이 머지 진행했어. 다음 개발 작업 진행하기전에 할게 있어. 1. 로깅 작업을 진행할꺼야. 로깅은 매일매일의 롤링 파일로 작업이 되어야하며, logback 프레임워크 http://logback.qos.ch/를 이용하여 다음의 로깅 작업을 합니다.
- 로그 파일을 하루 단위로 분리합니다.
- 로그 내용에 따라 적절한 로그 레벨을 적용합니다.
- 오류 발생 시, StackTrace 전체를 로그 파일에 남깁니다.
로깅은 쓰레드를 통해서 비동기로 작업해야하지 않을까 싶어. 어떻게 생각해?

## Assistant
AsyncAppender 기반 비동기 로깅 권장. 설정 파일 부재 확인 후 계획 제시.

## User
1, backend-YYYY-MM-DD.log로 기록해줘. 2. 로그레벨은 너가 추천하는 것으로 진행해줘. 3. 이것도 너가 추천하는 것으로 진행해줘. 또한, 이거 pr 만들 때 prompt 내용 기록해야돼. 알지?

## Assistant
Plan 수립 후 구현 요청 확인.

## User
Implement the plan.
