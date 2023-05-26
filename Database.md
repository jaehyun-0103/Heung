테이블명: Users

| Column Name   | Data Type | Description     |
|---------------|-----------|-----------------|
| user_id       | 문자열    | 사용자 식별자     |
| user_nickname | 문자열    | 사용자 닉네임     |

테이블명: Posts

| Column Name | Data Type  | Description      |
|-------------|------------|----------------- |
| post_id     | 문자열     | 게시글 식별자      |
| user_id     | 문자열     | 사용자 식별자      |
| post_title  | 문자열     | 게시글 제목        |
| post_content| 문자열     | 게시글 내용        |
| post_date   | 문자열     | 게시글 작성 일시   |

테이블명: Recruits

| Column Name    | Data Type  | Description      |
|----------------|------------|------------------|
| recruit_id     | 문자열     | 모집글 식별자      |
| user_id        | 문자열     | 사용자 식별자      |
| recruit_title  | 문자열     | 모집글 제목        |
| recruit_content| 문자열     | 모집글 내용        |
| recruit_date   | 문자열     | 모집글 작성 일시   |

테이블명: Comments

| Column Name    | Data Type  | Description        |
|----------------|------------|--------------------|
| comment_id     | 문자열     | 댓글 식별자         |
| post_id        | 문자열     | 게시글 식별자       |
| user_id        | 문자열     | 사용자 식별자       |
| comment        | 문자열     | 댓글 내용           |
| comment_date   | 문자열     | 댓글 작성 일시      |

테이블명: Likes

| Column Name | Data Type | Description          |
|-------------|-----------|----------------------|
| like_id     | 문자열    | 추천 식별자           |
| post_id     | 문자열    | 게시글의 식별자        |
| user_id     | 문자열    | 사용자 식별자          |

테이블명: Recruitment

| Column Name       | Data Type  | Description      |
|-------------------|------------|------------------|
| recruitment_id    | 문자열     | 모집 신청 식별자   |
| recruit_id        | 문자열     | 모집글의 식별자    |
| user_id           | 문자열     | 사용자의 식별자    |
| recruit_status    | 문자열     | 참여 신청 상태     |

테이블명 : Calendar

| Column Name | Data Type | Description      |
|-------------|-----------|------------------|
| user_id     | 문자열    | 사용자 식별자      |
| date        | 문자열    | 사용자 닉네임      |
| endTime     | 문자열    | 게시글 식별자      |
| location    | 문자열    | 게시글 제목        |
| startTime   | 문자열    | 게시글 내용        |
| title       | 문자열    | 게시글 작성 일시   |

