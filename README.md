## Dependencies

The work on the project consists of the following

1. Java 1.8
1. [PostgreSQL 9.+](https://www.codementor.io/devops/tutorial/getting-started-postgresql-server-mac-osx) 

## Configure environment

### Postgresql
1. `brew install postgresql`
1. `psql postgres`
1. Under `psql`-console type the following
    ```postgresql
    create role wslogin with login password 'wslogin';
    create database wslogin;
    GRANT ALL PRIVILEGES ON DATABASE wslogin TO wslogin;

    \q
    ```
1. `psql wslogin`
1.  Under `psql`-console type `create extension citext`;

## Build, launch, tests

- To test launch via command line `./gradlew test`

## TODO
- fix integration tests
- wide tests
- create separate session for separate logged in user
- connect messages via sequence_id vice versa
- configure web sockets via `.yaml`
