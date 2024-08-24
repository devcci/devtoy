# [DEVTOY] 커머스 토이 프로젝트

## 프로젝트 환경

- JAVA 17
- Spring Boot 3.3.2
- Gradle 8.8
- Multi-Module Project

### 라이브러리

| 라이브러리                         | 용도                                          |
|-------------------------------|---------------------------------------------|
| Spring Web                    | MVC API 개발                                  |
| Spring Cloud Gateway(webflux) | - MSA 환경에서의 라우팅과 로드밸런싱 <br/> - 통합 인증/인가     |
| Spring Cloud Eureka           | 로드밸런싱을 위한 서비스 인스턴스 정보 제공을 위한 디스커버리 서비스      |
| Spring Data JPA               | ORM                                         |
| Spring Data Redis(Lettuce)    | - 회원 Refresh Token 캐싱<br/> - 프로젝트 통합 캐시 저장소 |
| Spring Cache                  | 캐시매니저 추상화(Redis 연동)                         |
| MySQL                         | DB                                          |
| Kafka                         | 주문 - 요청 메시징(예정)                             |
| JJWT                          | 인증/인가를 위한 JWT 토큰                            |
| Lombok                        | 개발 편의성                                      |
| Docker                        | Docker Compose 활용                           |
| TestContainer                 | 통합 테스트 편의성과 데이터 멱등성 보장                      |
| Swagger-ui                    | API 명세서                                     |

## [API 명세서]

- [Postman](https://documenter.getpostman.com/view/17962382/2sA3sAh7di)
- [Swagger](http://localhost:8080)(localhost 호출이므로 프로젝트 실행 필요)

- 테스트 유저 정보
    - memberId: tester1
    - password: test1234

## [프로젝트 실행 방법]

- 실행을 위해 Docker 및 Docker Compose가 필요합니다.
- 프로젝트 루트(docker-compose.yml 파일의 위치)로 이동 후 아래 명령어 실행

### infra 실행 후 모듈별 실행

```bash
docker-compose -f docker-compose-infra.yml up -d
```

```
1. devtoy-eureka 실행
2. devtoy-gateway 실행
3. devtoy-member || devtoy-product || devtoy-order 실행
```

### 전체 실행

```bash
docker-compose up -d
```
