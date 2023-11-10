# Team258 Library Project
![스크린샷 2023-10-11 오후 2 25 16](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F480983fb-409a-4952-8a68-ff432e008927%2F2b64ef5d-724f-471d-a665-06fdfc25330b%2FUntitled.png?table=block&id=4244553b-a141-46ac-b5a3-ce9a5fa56a1a&spaceId=480983fb-409a-4952-8a68-ff432e008927&width=2000&userId=0f23fed0-d3bf-4d0e-83bc-1ecb8c4e0e9e&cache=v2)

## 🖥️ 프로젝트 누리책마루 소개
누리는 세상을, 마루는 모이는 공간을 의미하여, 세상의 책이 모여 순환하는 곳이라는 뜻입니다.
`Spring Boot`를 활용한 도서관 웹 서비스 팀 프로젝트입니다.
- ### PROJECT Brochure
https://ohnyong.notion.site/33063e8c683c4e63aa4db53158ecf12d?pvs=4

## 🕰️ 개발 기간
* 23.10.04 ~ 23.11.15

## 🧑‍🤝‍🧑 맴버구성
- 팀장 : 김인용 | https://github.com/yzpocket | https://www.citefred.com
- 팀원 : 송유헌 | https://github.com/songyuheon98 | https://velog.io/@songyuheon
- 팀원 : 조영익 | https://github.com/yame123 | https://inyasang.tistory.com
- 팀원 : 정강용 | https://github.com/tuto3355 | https://naktaa.tistory.com
- ### TEAM BLOG
https://ohnyong.notion.site/TechBlog-TEAM-258-1fcb896ca14947c58d46473f1f7436bd?pvs=4

## ⚙️ 개발 환경
- **MainLanguage** : `Java - JDK 17`
- **IDE** : `IntelliJ IDEA Ultimate`
- **Framework** : `SpringBoot`, `SpringSecurity`, `JPA / JPQL + QueryDSL`
- **Database** : `RDS - MySQL`, `AWS EC2 - ELK`
- **DevOPS** :
    - **HTTPS** : `AWS ROUTE 53`, `ACM`, `ALB`
    - **CI/CD** : `EC2` - `S3`, `CodeDeploy`, `Github Action`
- **STREAMING/MESSAGING** `Kafka-Redis`
- **TEST** : `POSTMAN API Request`, `JUnit5`, `JMeter`

## 📌 주요 기능
- **관리자 기능**
    - **도서 나눔 이벤트 관리**
    - 사용자관리
    - 도서관리
- **사용자 기능**
    - **도서 검색 기능**
    - 도서 대출/예약 기능
    - 내 정보 조회
    - 대출/예약 내역 조회

## 📝 아키텍처
### User Flow Chart
![스크린샷 2023-10-11 오후 2 25 16](https://github.com/yzpocket/spring-cafeservice/assets/67217259/839b1f28-55af-48e0-9e5b-c26bdb4b1dcc)

### ERD
![스크린샷 2023-10-11 오후 9 56 59](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F480983fb-409a-4952-8a68-ff432e008927%2F499be57f-545e-4ad9-be84-a4c6b3d9b899%2FUntitled.png?table=block&id=3005fc3b-1edc-40ee-b121-b4220555671d&spaceId=480983fb-409a-4952-8a68-ff432e008927&width=2000&userId=0f23fed0-d3bf-4d0e-83bc-1ecb8c4e0e9e&cache=v2)

### Project Architecture
![스크린샷 2023-10-05 오전 8 43 39](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F480983fb-409a-4952-8a68-ff432e008927%2F32bde6f4-65d6-42d2-8e3e-d9003b2eefcd%2FUntitled.png?table=block&id=e6978fb5-b323-49bf-b99b-e29cc042a884&spaceId=480983fb-409a-4952-8a68-ff432e008927&width=2000&userId=0f23fed0-d3bf-4d0e-83bc-1ecb8c4e0e9e&cache=v2)
