##Web Socket Login Backend Module

###Build process

- `mvn clean install` or `mvn clean install -Dmaven.test.skip=true`
- `java -jar target/auth-0.0.1.jar`

###Test process

- `mvn clean test`

###Production deploy

- You should add datasource for production profile, you can see `com.eustimenko.services.auth.configuration.DatasourceConfiguration.mongoTemplate`.
- Also, you can correct `application.yaml` to add mongo datasource for production profile by [spring boot configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

### Additional Information
Use `base64-encoding` to store passwords, see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.encode
