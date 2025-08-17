# 게시판 API

Spring Boot를 이용한 게시판 REST API

## 기술 스택
- Java 17, Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA, MariaDB

## API 엔드포인트
- POST /api/auth/signup - 회원가입
- POST /api/auth/signin - 로그인
- GET/POST/PUT/DELETE /api/boards - 게시글 관리
- POST/PUT/DELETE /api/boards/{id}/comments - 댓글 관리
