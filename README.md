# schedule-management
일정 관리 시스템

### 실행방법
1. source code clone
2. 접근 할 외부 혹은 로컬 DB 에 'sparta_schedule' schema 생성
   - charset : utf8mb4
   - collation : utf8mb4_unicode_ci 
3. 환경변수 설정 후 Application 실행
   - Environment Variable(ex) url=localhost:3306;username=root;password=qwe123!@#;salt=todo )
     - url = db 주소
     - username = db username
     - password = user 의 password
     - salt = password encoding 시 필요한 salt 키 임의 설정