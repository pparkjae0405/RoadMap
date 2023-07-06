## 로드맵
  - 개발자 로드맵 정보 플랫폼

<br>

## 📆 개발 기간
  - 2023.04 ~ 


<br>

## 🛠 기술 스택
  - Front End - [`김회창`](https://github.com/kimhaechang1)
    - React, React-query, TypeScript
    
  - Back End - [`박재민`](https://github.com/pparkjae0405), [`신성철`](https://github.com/ShinSeongCheol)
    - Java 17 
    - SpringBoot 3.0.6, Spring Data JPA, Spring Security, MySQL

<br>

## ✏ 자료
  - [`페이지 레이아웃`](https://www.figma.com/file/bSCwaFwRcLgUVtGNYk3ys4/main-page?node-id=0%3A1&t=KQI9Tj4tQEbmReBb-1)

![figma](https://user-images.githubusercontent.com/62460178/236998538-332ba792-a28c-4c47-b52f-fb122d8df223.png)

<br>

  - [`DB`](https://www.notion.so/DB-705f370de9fc4874966ca92ac3c47bbf?pvs=4)

<br>

  - [`API`](https://www.notion.so/API-6ec29e24a50a4e0191340d9d186985bb?pvs=4)

<br>

## 🐱‍💻 백엔드 역할 분담
  - 박재민
    - 글(Roadmap, Info, Tag)
    - 댓글(Comment)
  - 신성철
    - 유저(User)

<br>
  
## 💡 커밋 메세지 규칙

```
{타입}: {메세지}

{본문}
```

### 타입

- feat: 기능 추가
- fix: 수정
- refactor: 코드 리팩토링
- test: 테스트

### 메세지

- 해당 커밋에 대한 설명문

### 본문

- {타입}: {메세지} 로 표현할 수 없는 상세 내용을 적는 부분
- 따라서, {타입}: {메세지} 로 표현이 가능하다면 생략 가능

<br>

## 👩🏻‍💻 작업 방식

### Repository 주인

선행조건 : 프로젝트 생성, 중앙 remote에 업로드, 공동 작업자 추가, branch 보호 설정

공동 작업자의 pull request 요청이 있다면 코드리뷰하여 충돌이 있는지 확인 후 승인

1. 기능 개발에 대해 feature/기능-개발로 branch 생성 후 작업
    - branch 생성 이름 : feature/기능-개발
    - 띄어쓰기 시 사이에 “-” 추가
    - 커밋 메세지 규칙에 따라 커밋
2. 로컬에서만 작업하면 공유가 안되니 중앙 remote로 push
(최소 하루에 한번은 내 작업을 커밋하여 푸시)
3. 1,2번이 완료되면 pull request를 보내 merge 요청
4. 코드 리뷰하여 충돌이 있으면 해결 후 중앙 remote의 main branch에 내 작업물을 merge
5. merge되었다면 main branch로 이동하여 로컬-중앙 remote 코드 간 pull으로 동기화
6. 로컬&중앙 remote의 feature/기능-개발 branch 삭제

### 공동 작업자

선행조건 : 공동 작업자 요청 승인, 프로젝트 clone

1. 기능 개발에 대해 feature/기능-개발로 branch 생성 후 작업
    - branch 생성 이름 : feature/기능-개발
    - 띄어쓰기 시 사이에 “-” 추가
    - 커밋 메세지 규칙에 따라 커밋
2. 로컬에서만 작업하면 공유가 안되니 중앙 remote로 push
(최소 하루에 한번은 내 작업을 커밋하여 푸시)
3. 1,2번이 완료되면 pull request를 보내 merge 요청
4. 코드 리뷰하여 충돌이 있으면 해결 후 중앙 remote의 main branch에 내 작업물이 merge됨
5. merge되었다면 main branch로 이동하여 로컬-중앙 remote 코드 간 pull으로 동기화
6. 로컬&중앙 remote의 feature/기능-개발 branch 삭제