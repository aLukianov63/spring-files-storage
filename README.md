# Spring Boot files server

Example of file storage REST API, with local machine implementation and S3 bucket

## 🚧 Common setup

Clone the repo and install the dependencies.

```bash
git clone https://github.com/aLukianov63/spring-files-storage.git
```

## 🗃️ Requirements

* Spring Boot
    * Spring WEB
    * Spring Data JPA
* PostrgreSQL Driver
* AWS SDK For Java
* Apache Commons IO
* Lombok

## 🛠️ Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<host>:<port>/<database-name>
    username: <username>
    password: <password>
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 20MB

file-storage:
  local:
    directory: <local path>
  s3:
    endpoint: <endpoint>
    region: <region>
    access-key: <access-key>
    secret-key: <secret-key>
    bucket-name: <bucket-name>
```

## 📝 License

Distributed under the MIT license. See `LICENSE` for more information.