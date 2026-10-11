# Modol 10 test - learning application how to write integration test in spring boot project.

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
  - `service/` - business logic (`CarServiceInter` interface, `CarService` implementation)
  - `model/` - domain classes (`Car`, `Color` enum)
  - `converColor/ConvertColor.java` - converts a color string from the request to the `Color` enum
  - `exception/` - custom exceptions (`CarNotExist`, `ColorNotFound`) with `@ControllerAdvice` handlers
- `src/main/resources/` - `application.properties` and README images (`img/`)
- `src/test/java/iwona/pl/modol10test/` - tests
  - `controller/CarApiTest.java` - MockMvc tests of the REST layer
  - `service/CarServiceTest.java` - service tests
  - `Modol10TestApplicationTests.java` - Spring context load test
- `pom.xml` - Maven build and dependencies
- `README.md` - project description

## Workflow

### Build
There is no Maven wrapper in this repo; use a locally installed `mvn`.

- `mvn clean package`: compile (also acts as the type check), run tests, build the JAR
- `mvn spring-boot:run`: run the app locally

### Tests
- `mvn test`: run all tests (Surefire, `*Test.java` / `*Tests.java`)
- `mvn test -Dtest=CarApiTest`: run a single test class

### Before committing
- `mvn clean verify`: full build with all tests

## Constraints
- Never modify `src/main/java/iwona/pl/modol10test/model/Car.java` without asking first.
- Do not install new dependencies without asking first
- Never commit secrets, passwords, tokens, or API keys. Use environment variables or placeholders.
- Never delete or disable existing tests (`@Disabled`, commenting out) to make the build pass.
- Never weaken a test assertion to make it pass. Fix the code or report the problem.