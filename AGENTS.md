# Modol 10 test - learning application how to write integration test in spring boot project.

## Project Overview
A small Spring Boot REST API for managing cars (`/api/cars`), built to practice writing tests in a
Spring Boot project: MockMvc integration tests of the controller and unit tests of the service.
Data lives in memory (3 seed cars created in `CarService`), so there is no database and changes are lost on restart.

Endpoints: list, get by id, get by color, create (`POST /new`), replace (`PUT /modify/{id}`),
change color / mark (`PATCH`), delete. Missing cars and unknown colors return 404 through
`@ControllerAdvice` handlers; invalid request bodies return 400.

## Tech Stack
- Spring Boot 2.2.6, Spring Web (REST)
- Java 8
- Maven
- JUnit 5 (Jupiter), Mockito, MockMvc, AssertJ, Hamcrest

## Code Style
- `Optional` only as a return type. Never as a field or method parameter.
- Never return `null` from collections. Return an empty collection.
- Use specific custom exceptions (e.g. `ColorNotFound`). Never catch and swallow exceptions.
- Constructor injection only. No `@Autowired` on fields.
- Validate input with Bean Validation (`@Valid`, `@NotNull`) on request DTOs.

## Project Structure
Spring Boot (Maven, Java 8) REST API for cars. Base package: `iwona.pl.modol10test`.

- `src/main/java/iwona/pl/modol10test/` - application code
  - `Modol10TestApplication.java` - Spring Boot entry point
  - `controller/CarApi.java` - REST controller (`/api/cars`: GET, POST, PUT, PATCH, DELETE)
  - `service/` - business logic (`CarServiceInter` interface, `CarService` implementation with an in-memory list of 3 seed cars, no dependencies)
  - `model/` - domain classes (`Car`, `Color` enum)
  - `converColor/ConvertColor.java` - converts a color string from the request to the `Color` enum (used by `CarApi`)
  - `exception/` - custom exceptions (`CarNotExist`, `ColorNotFound`) with `@ControllerAdvice` handlers
- `src/main/resources/` - `application.properties` and README images (`img/`)
- `src/test/java/iwona/pl/modol10test/` - tests
  - `controller/CarApiTest.java` - integration tests of the REST layer (`@SpringBootTest` + MockMvc); each test uses `@DirtiesContext` because the service keeps mutable in-memory state
  - `service/CarServiceTest.java` - plain JUnit 5 unit tests of the real `CarService` (no Spring, no mocks)
  - `Modol10TestApplicationTests.java` - Spring context load test
- `pom.xml` - Maven build and dependencies
- `README.md` - project description

## Workflow

### Build
There is no Maven wrapper in this repo; use a locally installed `mvn`.

- `mvn clean package`: compile (also acts as the type check), run tests, build the JAR
- `mvn spring-boot:run`: run the app locally (http://localhost:8080/api/cars)

### Tests
- `mvn test`: run all tests (Surefire, `*Test.java` / `*Tests.java`)
- `mvn test -Dtest=CarApiTest`: run a single test class
- If Maven cannot download surefire 2.22.2 (SSL / PKIX error), use the cached plugin offline:
  `mvn -o test-compile org.apache.maven.plugins:maven-surefire-plugin:3.5.2:test`
  (and `mvn -o spring-boot:run` to start the app)
- Current status: 28 tests (16 `CarApiTest`, 11 `CarServiceTest`, 1 context test), all passing

### Before committing
- `mvn clean verify`: full build with all tests

## Constraints
- Never modify `src/main/java/iwona/pl/modol10test/model/Car.java` without asking first.
- Do not install new dependencies without asking first
- Never commit secrets, passwords, tokens, or API keys. Use environment variables or placeholders.
- Never delete or disable existing tests (`@Disabled`, commenting out) to make the build pass.
- Never weaken a test assertion to make it pass. Fix the code or report the problem.