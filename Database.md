테이블명: Users

| Column Name   | Data Type | Description     |
|---------------|-----------|-----------------|
| user_id (pk)  | 문자열    | 사용자 식별자     |
| user_nickname | 문자열    | 사용자 닉네임     |

테이블명: Posts

| Column Name | Data Type  | Description      |
|-------------|------------|----------------- |
| post_id (pk)| 문자열     | 게시글 식별자      |
| user_id (fk)| 문자열     | 사용자 식별자      |
| post_title  | 문자열     | 게시글 제목        |
| post_content| 문자열     | 게시글 내용        |
| post_date   | 문자열     | 게시글 작성 일시   |

테이블명: Recruits

| Column Name    | Data Type  | Description     |
|----------------|------------|-----------------|
| recruit_id (pk)| 문자열     | 모집글 식별자     |
| user_id (fk)   | 문자열     | 사용자 식별자     |
| recruit_title  | 문자열     | 모집글 제목       |
| recruit_content| 문자열     | 모집글 내용       |
| recruit_date   | 문자열     | 모집글 작성 일시  |
| recruit_endDate| 문자열     | 모집글 종료 일시  |
| recruit_max    | 문자열     | 모집 인원        |
| recruit_curr   | 문자열     | 현재 모집 인원    |
| recruit_type   | 문자열     | 모집 유형         |

테이블명: Comments

| Column Name    | Data Type  | Description    |
|----------------|------------|----------------|
| comment_id (pk)| 문자열     | 댓글 식별자     |
| post_id (fk)   | 문자열     | 게시글 식별자   |
| user_id (fk)   | 문자열     | 사용자 식별자   |
| comment        | 문자열     | 댓글 내용       |
| comment_date   | 문자열     | 댓글 작성 일시  |

테이블명: Likes

| Column Name | Data Type | Description    |
|-------------|-----------|----------------|
| like_id (pk)| 문자열    | 추천 식별자      |
| post_id (fk)| 문자열    | 게시글의 식별자  |
| user_id (fk)| 문자열    | 사용자 식별자    |
| like        | 문자열    | 추천수          |

테이블명 : Calendar

| Column Name  | Data Type | Description |
|--------------|-----------|-------------|
| cal_id (pk)  | 문자열    | 캘린더 식별자 |
| user_id (fk) | 문자열    | 사용자 식별자 |
| cal_title    | 문자열    | 일정 제목     |
| cal_memo     | 문자열    | 일정 메모     |
| cal_date     | 문자열    | 작성 날짜     |
| cal_startDate| 문자열    | 일정 시작일   |
| cal_endDate  | 문자열    | 일정 종료일   |
| cal_location | 문자열    | 일정 위치     |
