테이블명: Users

| Column Name   | Data Type | Description  |
|---------------|-----------|--------------|
| user_id (pk)  | String    | 사용자 식별자 |
| user_nickname | String    | 사용자 닉네임 |

테이블명: Posts

| Column Name | Data Type| Description    |
|-------------|----------|----------------|
| post_id (pk)| String   | 게시글 식별자   |
| user_id (fk)| String   | 사용자 식별자   |
| post_title  | String   | 게시글 제목     |
| post_content| String   | 게시글 내용     |
| post_date   | String   | 게시글 작성 일시|

테이블명: Recruits

| Column Name    | Data Type| Description    |
|----------------|----------|----------------|
| recruit_id (pk)| String   | 모집글 식별자   |
| user_id (fk)   | String   | 사용자 식별자   |
| recruit_title  | String   | 모집글 제목     |
| recruit_content| String   | 모집글 내용     |
| recruit_date   | String   | 모집글 작성 일시|
| recruit_endDate| String   | 모집 종료 일시  |
| recruit_max    | String   | 모집 최대 인원  |
| recruit_curr   | String   | 현재 모집 인원  |
| recruit_type   | String   | 모집 유형       |
| recruit_session| String   | 세션 유형       |
| recruit_class  | String   | 클래스 유형     |
| recruit_contact| String   | 작성자 연락처   |

테이블명: Comments

| Column Name    | Data Type| Description  |
|----------------|----------|--------------|
| comment_id (pk)| String   | 댓글 식별자   |
| post_id (fk)   | String   | 게시글 식별자 |
| user_id (fk)   | String   | 사용자 식별자 |
| comment        | String   | 댓글 내용     |
| comment_date   | String   | 댓글 작성 일시|

테이블명 : Reply

| Column Name    | Data Type| Description    |
|----------------|----------|----------------|
| reply_id (pk)  | String   | 대댓글 식별자   |
| comment_id (fk)| String   | 댓글 식별자     |
| user_id (fk)   | String   | 사용자 식별자   |
| reply          | String   | 대댓글 내용     |
| reply_date     | String   | 대댓글 작성 일시|

테이블명: Likes

| Column Name | Data Type   | Description              |
|-------------|-------------|--------------------------|
| post_id (fk)| String      | 게시글의 식별자           |
| userIds     | String Array| 좋아요를 누른 사용자 식별자|
| like        | String      | 좋아요 수                 |

테이블명 : Calendar

| Column Name  | Data Type| Description |
|--------------|----------|-------------|
| cal_id (pk)  | String   | 캘린더 식별자|
| user_id (fk) | String   | 사용자 식별자|
| cal_title    | String   | 일정 제목    |
| cal_memo     | String   | 일정 내용    |
| cal_date     | String   | 작성 날짜    |
| cal_startDate| Long     | 일정 시작일  |
| cal_endDate  | Long     | 일정 종료일  |
| cal_location | String   | 일정 위치    |

테이블명 : Store

| Column Name| Data Type| Description|
|------------|----------|------------|
| name       | String   | 가게 이름   |
| address    | String   | 주소        |
| location   | GeoPoint | 위도와 경도 |
| district   | String   | 행정동      |
| phoneNumber| String   | 전화번호    |
| content    | String   | 가게 유형   |
