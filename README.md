# GAD API Test Automation Framework

This repository contains automated API tests for the [GAD GUI API demo](https://github.com/jaktestowac/gad-gui-api-demo). The tests run against a separately started instance of that API. This repository contains the test framework; it does not start the API server.

## Technology

- Java 25
- Maven
- TestNG
- Rest Assured for HTTP requests and response checks
- Gson for JSON serialization and deserialization
- Allure TestNG and Rest Assured integrations for test reporting
- Log4j 2 for test logging
- Datafaker for generated user data
- OWNER for test configuration

## Project structure

```text
.
├── pom.xml                              # Maven dependencies, Java version, and build plugins
├── eclipse-formatter.xml                # Java formatting rules used by Spotless
├── eclipse-formatter.importorder        # Java import ordering used by Spotless
└── src
    ├── main/java
    │   ├── dto/                          # Generated user-data DTO and factory
    │   └── models/                       # Login and users API request/response models
    └── test
        ├── java
        │   ├── api_adapters/             # Rest Assured request methods and shared specs
        │   ├── config/                   # Base URL configuration interface
        │   ├── listeners/                # Test logging and retry handling
        │   └── tests/                    # Test base class and Users API tests
        └── resources
            ├── config.properties.example # Example local test configuration
            ├── json_schemas/             # Schemas for GET user responses
            ├── allure.properties         # Allure results output location
            ├── api_tests_suit.xml        # TestNG suite descriptor (currently references an old package/group)
            └── log4j2-test.xml           # Test logging configuration
```

## What is covered

`tests.UsersTest` exercises these user API flows:

- `GET /api/users`: validates the response against `get_users_schema.json`.
- `POST /api/users`: creates a user and checks the returned fields and assigned ID.
- `GET /api/users/{id}`: retrieves a created user and validates its response schema.
- `PATCH /api/users/{id}`: logs in, sends an authorized update, then checks changed values.
- `DELETE /api/users/{id}`: logs in, deletes a created user, and confirms a later lookup returns 404.

The `LoginAdapter` provides `POST /api/login` to obtain the bearer token used by authorized operations. User payloads are generated with Datafaker. `TestListener` attaches a retry analyzer to tests; a test can be retried up to two times, and a failure that passes on retry is removed from the final failed-test results.

## Prerequisites and configuration

- JDK 25
- Node to run GAD
- Maven

The configured base URL is read from the `baseUrl` property. Create a local configuration file from the example and set the URL for your API instance:

```sh
cp src/test/resources/config.properties.example src/test/resources/config.properties
```

Edit `src/test/resources/config.properties` as needed. The example points to `http://localhost:3000`. Alternatively, set the base URL on the command line:

```sh
mvn test -DbaseUrl=http://localhost:3000
```

## Allure reports

Run the tests first so Allure has result files to render. With the API running and test configuration set as described above.

## Formatting

Spotless is configured to apply the repository's Eclipse Java formatter during Maven's `validate` phase. The formatter settings are in `eclipse-formatter.xml` and `eclipse-formatter.importorder`.
