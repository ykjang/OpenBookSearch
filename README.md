

### OPEN API 기반 도서검색 어플리케이션 

------



#### 어플리케이션 소개

- 본 어플리케이션은 OpenAPI를 활용하여 도서검색 서비스를 제공하는 API 어플리케이션입니다. 

- 어플리케이션에 사용되는 오픈API는 카카오의  책검색 API를 사용합니다.
  - API URL - https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-book
- 카카오 API를 사용할 수 없는 경우, Naver 책검색 API로 대체해서 검색결과를 제공합니다.
  - API URL - https://developers.naver.com/docs/search/book/

#### 어플리케이션 개발환경

- JDK: OpenJDK 11.0.2
- 언어,프레임워크: SpringBoot 2.2.6
- Datasource: H2 (In-Memory Database)
- ORM - JPA( Hibernate 5)
- 빌드, 의존성 라이브러리 관리: gradle
- Test Tool: JUnit5, Mokito

------

#### 외부 라이브러리

- **lombok - 어노테이션기반 자동코드 지원**

  - 소스에서 사용되는 setter, getter 메서드 생성, 생성자 메서드 생성들의 작업들은 lombok 기반의 어노테이션을 사용하였습니다.
  - 어노테이션 기반으로 코드의 반복작업을 줄여주기 때문에,  코드량이 획기적으로 줄어들고 가독성도 높아집니다.
  - build.gradle 의존성 라이브러리

    ```properties
    dependencies {
      ...
      compileOnly 'org.projectlombok:lombok'
      annotationProcessor 'org.projectlombok:lombok'
      ...
    }
    ```

- **spring-session - 세션관리**

  - spring-session 라이브러리를 사용하면 어플리케이션의 세션정보를 외부저장소인  Redis,MongDB, JDBC(DB)에 저장이 가능합니다. 
  - 본 어플리케이션은 로그인 사용자정보를 spring-session JDBC를 통해 기본 데이터소스저장소인 h2에 저장하도록 설정되었습니다.
  - 독립적으로 구동될 수 있는 어플리케이션 환경을 위해 별도의 서버구동이 필요한 Redis나 MongoDB는 적용하지 않았습니다. 
  - 의존성 라이브러리

    ```properties
    dependencies {
      ...
      implementation 'org.springframework.session:spring-session-jdbc'
      ...
    }
    
    ```

- **jbcrpto - 암호화** 

  - 회원가입 및 로그인시 사용되는 비밀번호의 암호화에 사용되는 라이브러리입니다. Spring Security를 적용하지 않았기 때문에 별도의 외부라이브러를 통해 사용자 비밀번호를 암호화 합니다.

  - 암호화방식은 단방향 해싱기법으로 암호화할때마다 임의의 난수를 발생시켜 그 값을 Key로 암호화하기 때문에, 복호화가 불가능한 알고리즘을 사용합니다. 

    ```properties
    dependencies {
      ...
      implementation group: 'org.mindrot', name: 'jbcrypt', version: '0.4'
      ...
    }
    
    ```

------

### 어플리케이션 제공기능 

모든 기능은 API를 통해 제공됩니다. 

1. 회원가입
  - 아이디와 비밀번호를 통해 회원가입이 가능합니다.
2. 로그인
  - 가입한 아이디와 비밀번호로 로그인을 할 수 있습니다.
  - 로그인이 성공하면 회원정보가 세션에 저장됩니다. 
3. 책검색 기능 
  - 로그인이 되어 있어야 사용이 가능합니다.
  - 키워드로 책 검색이 가능합니다. 
  - 검색결과는 페이징처리되어 제공됩니다.
4. 나의 책검색 히스토리 조회
  - 로그인이 되어 있어야 사용이 가능합니다.
  - 내가 검색한 이력을 조회할 수 있습니다.
  - 이력정보는 최신일자순으로 제공되며 검색일자와 검색키워드 정보를 보여줍니다.
5. 검색 키워드 TOP10 조회
  - 로그인이 되어 있어야 사용이 가능합니다.
  - 가장 많이 사용된 검색 키워드 TOP 10 을 조회할 수 있습니다.
  - 키워드와 검색횟수 정보를 제공합니다. 



#### 프로젝트 실행 방법

- 프로젝트 빌드 및 실행

  ```
  $ git clone git@github.com:ykjang/OpenBookSearch.git
  $ cd OpenBookSearch
  $ ./gradlew clean bootJar
  $ java -jar -DSpring.profiles.active=local build/libs/booksearch-0.0.1-SNAPSHOT.jar 
  ```
- 참고사항
   - H2 DB의 DDL-AUTO 설정이 `CREATE`로 되어 있기 때문에, 어플리케이션을 구동할때마다 DB 스키마가 Drop되고 다시 생성됩니다. 
   - 최초 구동 후 DB스키마를 유지하고 싶다만 DDL-AUTO 설정을 아래와 같이 `UPDATE`로 변경하면 됩니다. (변경된 스키마만 반영됩니다.)
  ```java
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      naming:
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
      ddl-auto: update
  ```

------

#### API 명세 및  테스트 방법

제공되는 모든 API의 응답객체의 데이타형식은 JSON입니다. 

- 공통응답객체

  | 속성명  | 유형   | 설명                                 |
  | ------- | ------ | ------------------------------------ |
  | code    | string | 응답결과 코드, 아래 공통코드 표 참조 |
  | message | string | 응답메세지                           |
  | data    | string | 응답데이타 (JSON전문)                |

  - Response Data Example ( 나의 책검색 이력조회)

  ```json
  {
      "code": "S000",
      "message": "정상적으로 처리되었습니다.",
      "data": [
          {
              "userId": "testuser1",
              "keyword": "자바 ",
              "createDate": "2020-04-19 19:48:55"
          },
          {
              "userId": "testuser1",
              "keyword": "spring",
              "createDate": "2020-04-19 19:48:36"
          }
      ]
  }
  ```

- API 목록

| API 서비스명      | API URI                                                      | Method |
| ----------------- | ------------------------------------------------------------ | ------ |
| 회원가입          | /api/v1/member                                               | POST   |
| 로그인            | /api/v1/login                                                | POST   |
| 책검색            | /api/v1/book/search/{userId}?keyword={검색어}&page={페이지번호}&size={페이지별검색건수} | GET    |
| 나의 책검색 이력  | /api/v1/book/search/{userId}/histories                       | GET    |
| 검색 키워드 TOP10 | /api/v1/book/keyword/top10                                   | GET    |

- API 테스트 방법

  모든 API는 curl 명령어를 통해 테스트가 가능합니다. 

  - 회원가입 

    - userId - testuser1

    ```shell
    curl --location --request POST 'http://localhost:8081/api/v1/member' \
    --header 'Content-Type: application/json' \
    --data-raw '{ "user_id": "testuser1", "password": "password" }'
    ```

  - 로그인

    - userId - testuser1

    ```shell
    curl --location --request POST 'http://localhost:8081/api/v1/login' \
    --header 'Content-Type: application/json' \
    --data-raw '{ "user_id": "testuser1", "password": "password" }'
    ```

  - 책검색 (로그인 필수)

    - userId - 로그인 유저ID
    - keyword - 자바 
    - page - 1
    - size - 20

    ```shell
    curl --location --request GET 'http://localhost:8081/api/v1/book/search?keyword=%EC%9E%90%EB%B0%94%20&page=1&size=20' \
    --header 'Content-Type: application/json'
    ```

  - 나의 책검색 이력조회 (로그인 필수, URL의 회원ID Path는 로그인한 유저ID와 동일해야 함)

    - userId - 로그인 유저ID
    - 좀더 다양한 이력데이타를 조회하기 위해서는 책검색 API를 다른 키워드로 여러번 수행합니다. 

    ```shell
    curl --location --request GET 'http://localhost:8081/api/v1/book/search/{userId}/histories' \
    --header 'Content-Type: application/json' 
    ```

  - 검색 키워드 Top10 목록 (로그인 필수)
    - 좀더 다양한 키워드 목록을 조회하기 위해서는 책검색 API를 중복을 포함한 다양한 키워드로 여러번 수행합니다. 

    ```shell
    curl --location --request GET 'http://localhost:8081/api/v1/book/keyword/top10' \
    --header 'Content-Type: application/json' 
    ```

- 공통코드 

  API 어플리케이션이 반환하는 공통 코드목록 

  | 어플리케이션 코드 | HttpStatus Code | 메세지                                                       |
  | ----------------- | --------------- | ------------------------------------------------------------ |
  | S001              | 200,201       | 정상적으로 처리되었습니다.                                   |
  | E000              | 400             | 세션이 만료되었습니다.다시 로그인 하세요.                                  |
  | E001              | 400             | 요청항목이 유효하지 않습니다.                                |
  | E002              | 405             | 지원하지 않는 메서드입니다.                                  |
  | E101              | 400             | 회원아이디가 이미 존재합니다.                                |
  | E102              | 400             | 회원아이디가 존재하지 않습니다.                                       |
  | E103              | 400             | 비밀번호가 틀렸습니다.                                       |
  | E201              | 400             | 책검색 API 호출에 실패했습니다.                              |
  | E900              | 500             | 서비스처리 도중 오류가 발생했습니다.계속 발생시 관리자에게 문의하시기 바랍니다. |
