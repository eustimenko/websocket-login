## Web Socket Login Backend Module

### I/O

#### Message format

```json
{
    "type": "TYPE_OF_MESSAGE" ,
    "sequence_id": "09caaa73-b2b1-187e-2b24-683550a49b23",
    "data" : {}
}
```

#### Error data description

```json
{
  "data":{
    "error_description":"Customer not found",
    "error_code":"customer.notFound"
  }
}

```

#### Success authentication request

```json
{
    "type":"LOGIN_CUSTOMER",
    "sequence_id":"a29e4fd0-581d-e06b-c837-4f5f4be7dd18",
    "data":{
        "email":"fpi@bk.ru",
        "password":"123123"
    }
}
```

#### Success authentication response

```json
{
    "type":"CUSTOMER_API_TOKEN",
    "sequence_id":"cbf187c9-8679-0359-eb3d-c3211ee51a15",
    "data":{
        "api_token":"afdd312c-3d2a-45ee-aa61-468aba3397f3",
        "api_token_expiration_date":"2015-07-15T11:14:30Z"
    }
}
```

#### Error authentication request

```json
{
    "type":"LOGIN_CUSTOMER",
    "sequence_id":"715c13b3-881a-9c97-b853-10be585a9747",
    "data":{
        "email":"123@gmail.com",
        "password":"newPassword"
    }
}
```

#### Error authentication response

```json
{
    "type":"CUSTOMER_ERROR",
    "sequence_id":"715c13b3-881a-9c97-b853-10be585a9747",
    "data":{
        "error_description":"Customer not found",
        "error_code":"customer.notFound"
    }
}
```

### Build process

- Modify `application.yaml` to set correct properties
- Add needed datasource to java code, see `com.eustimenko.services.auth.configuration.DatasourceConfiguration.mongoTemplate`
- `mvn clean install` or `mvn clean install -Dmaven.test.skip=true`
- `java -jar target/auth-0.0.1.jar`

### Tests

- `mvn clean test`

### Additional Information

Use `base64-encoding` to store passwords, see `org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.encode`
