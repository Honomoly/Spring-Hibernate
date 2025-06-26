Spring-Hibernate
================
Project For Study
-----------------
* Spring과 Hibernate ORM을 기반으로 한 백엔드 구성

&nbsp;

- - -
### 환경변수 파일 양식
```bash
# .env
# 프로젝트 루트 또는 빌드후 JAR파일과 같은 디렉토리에 설정

DATABASE_URL="jdbc:mysql://db-server:db-host/db-name"
DATABASE_USERNAME="db-username"
DATABASE_PASSWORD="db-password"

# 아래는 개발용 설정
DATABASE_DDL_OPTION="update"
DATABASE_SQL_LOG="true"
API_DOCS_ENABLED="true"
```

&nbsp;

- - -
### application.yml 세부옵션
- spring.jpa.hibernate.ddl-auto (또는.env의 DATABASE_DDL_OPTION)
    - *create* : 서버 시작시 DB스키마 새로 생성, 종료시 삭제 처리 (개발용)
    - *update* : DB스키마 변경사항만 반영 (개발용)
    - *validate* : DB스키마의 유효성만 확인 (서비스용)
    - *none* : 아무것도 하지 않음 (서비스용)

&nbsp;

- - -
### 프로젝트 구조 (src/main/)
- **참고** : DDD Layered Architecture
```text
java/com/honomoly/study/hibernate
├── controller                      # 1. Presentation Layer
│   └── ...
├── service                         # 2. Application Layer
│   └── ...
├── domain                          # 3. Domain Layer
│   ├── entity
│   │   └── ...
│   └── BaseEntity.java  # abstract class
├── repository                      # 4. Infrastructure Layer
│   └── ...
├── dto                             # 5. Data Transfer Object
│   ├── ...
│   ├── common
│   └── **Dto.java  # interfaces
├── bean                            # 6. Bean
│   └── ...
├── util                            # 7. Util Class
│   └── ...
├── exception                       # 8. Exception
│   └── ...
├── config                          # 9. Configuration
│   └── ...
└── HibernateApplication.java       # 10. Main
```

#### 1. Presentation Layer
- API 요청을 받아서 파싱 및 데이터 검증, 마지막으로는 응답데이터를 구성하여 반환하는 책임을 가진다.

#### 2. Application Layer
- 기본적으로 트랜잭션 제어, DTO 변환, Domain Layer의 비즈니스 로직 호출등을 담당하는 계층으로 알려져있다.
- 현 시스템에서는 간결함을 위해, 해당 Layer(Service)에서 Domain의 비즈니스 처리 역할을 대부분 위임받았다.
- 그 대신 DTO 변환 기능(Adapter)을 해당 계층이 아닌, DTO간에 직접 변환이 이루어지도록 구성하였다.

#### 3. Domain Layer
- 핵심적인 비즈니스 로직과 모델 규칙, 데이터 제어등의 책임을 지니고 있는 계층으로 알려져있다.
- 위에 언급했듯이 현 시스템에선 사실상 모델 규칙(Entity)만을 담당하고 있다.

#### 4. Infrastructure Layer
- 실제로 DB연결 및 CRUD 기능을 제공하는 기반 계층이다.

&nbsp;

*※ 여기까지가 Layered Architecture의 계층 구조이며, 아래부터는 부가기능에 따라 분류되었다.*

&nbsp;

#### 5. Data Transfer Object
- API 요청, 응답 인터페이스 정의(Request, Response) 및 서버 내부 데이터 전송 규격(Input, Output)을 정의한다.
- 그 외의 필요한 객체는 common 디렉토리에 모아둔다.

#### 6. Bean
- Spring이 관리하는 Bean객체의 집합이다.

#### 7. Util Class
- 따로 객체 생성이 불가능한, 완전히 static 요소만으로 구성된 유틸 클래스의 집합이다.
- Spring이 관리하지 않는, 별개로 존재하는 클래스(또는 객체)들이다.

#### 8. Exception
- 전역적으로 사용하는 예외처리 클래스이다.
- 해당 예외는 bean.GlobalExceptionHandler 에서 관리한다.

#### 9. Configuration
- Spring의 기반 설정을 모아둔다.

#### 10. Main
- 서버가 시작되는 최초 진입점이다.
- 내부적으로는 Spring 실행전에 .env의 환경변수값을 Dotenv를 통해 읽어와서 JVM 환경변수로 등록하는 과정(System.setProperty)을 포함한다.