# Library Management System API - Spring Boot

The Library Management System API is developed with Spring Boot to facilitate efficient management of books, patrons, and borrowing records.

### Project Structure:

The project is organized as follows:

- **src:**
  - **main:**
    - **java/com.example.librarymanagementsystem:**
      - `config`: Contains security configuration.
      - `controllers`: Houses REST API controllers.
      - `Dto`: Manages Data Transfer Objects.
      - `logging`: Handles custom logging logic.
      - `models`: Defines data models.
      - `repositories`: Manages database operations.
      - `services`: Contains business logic and services.

  - **test:**
    - **java/com.example.librarymanagementsystem:**
      - `controller testing`: Includes tests for controllers.
      - `repository testing`: Includes tests for repositories.


### Models (Entities):

#### **Book:**

- Attributes:
  - `title`: Title of the book.
  - `author`: Author of the book.
  - `isbn`: ISBN (International Standard Book Number) of the book.
  - `publication_year`: Year of publication.

#### **Patron:**

- Attributes:
  - `firstname`: First name of the patron.
  - `lastname`: Last name of the patron.
  - `username` (email): Email used as the username.
  - `password`: Patron's password.
  - `Role`: Role of the patron (User or Admin).

- Additional Information:
  - Implements `UserDetails` interface.

#### **BorrowingRecord:**

- Attributes:
  - `borrowedBook`: Book being borrowed.
  - `BorrowedBy`: Patron borrowing the book.
  - `borrowingDate`: Date when the book is borrowed.
  - `returnDate`: Expected return date of the book.


### Authentication:

- **Implementation:**
  - Authentication is achieved using JSON Web Tokens (JWT).
  - Users obtain a token by sending credentials to the `/api/patrons/authenticate` endpoint.
  - The obtained token must be included in the headers of subsequent requests for authorized access.

- **Authorization:**
  - All other endpoints require pre-authorization.
  - Authorization is enforced using `@PreAuthorize("hasAuthority('ROLE_USER')")`.
  - User role is required for access to protected resources.

### Endpoints:

#### **BookController (`/api/books`):**

- **GET All Books:**
    - Endpoint: `GET /api/books`
    - Description: Retrieve a list of all books.

- **GET Book by ID:**
    - Endpoint: `GET /api/books/{id}`
    - Description: Retrieve details of a specific book.

- **POST Add a Book:**
    - Endpoint: `POST /api/books`
    - Description: Add a new book to the library.

- **PATCH Edit a Book:**
    - Endpoint: `PATCH /api/books/{id}`
    - Description: Edit details of a specific book.

- **DELETE Remove a Book:**
    - Endpoint: `DELETE /api/books/{id}`
    - Description: Remove a book from the library.

#### **PatronController (`/api/patrons`):**

- **GET All Patrons:**
    - Endpoint: `GET /api/patrons`
    - Description: Retrieve a list of all patrons.

- **GET Patron by ID:**
    - Endpoint: `GET /api/patrons/{id}`
    - Description: Retrieve details of a specific patron.

- **DELETE Remove a Patron:**
    - Endpoint: `DELETE /api/patrons/{id}`
    - Description: Remove a patron from the library.

- **POST Register a Patron:**
    - Endpoint: `POST /api/patrons/register`
    - Description: Register a new patron to the library.

- **POST Authenticate a Patron:**
    - Endpoint: `POST /api/patrons/authenticate`
    - Description: Authenticate a patron and receive a JWT.

- **PATCH Edit a Patron:**
    - Endpoint: `PATCH /api/patrons/{id}`
    - Description: Edit details of a specific patron.

#### **BorrowingController (`/api/borrow`):**

- **POST Borrow a Book:**
    - Endpoint: `POST /api/borrow/{bookId}/patron/{patronId}`
    - Description: Record a new borrowing transaction.

#### **ReturningController (`/api/return`):**

- **POST Return a Book:**
    - Endpoint: `POST /api/return/{bookId}/patron/{patronId}`
    - Description: Record a book return transaction.

### Data Transfer Objects (DTOs):

#### Dao:

- **AuthenticationResponse:**
    - Response carrying a JWT when registering and authenticating.

- **AuthenticationRequest:**
    - Request object carrying username and password for authentication.

- **RegisterRequest:**
    - Request object carrying firstname, lastname, username, and password for registration.

#### Dto:

- **BookUpdateDto:**
    - Request object carrying information about the book to be updated.

- **UserProfileUpdateDto:**
    - Request object carrying information about the patron to be updated (firstname, lastname, password).

*Note: All requests require pre-authorization (@PreAuthorize("hasAuthority('ROLE_USER')")) except for the register and authenticate endpoints.*

### Exception Handling:

The project employs exception handling in key controllers.
The primary exceptions and their corresponding responses are as follows:


#### Borrowing Controller:

- **Not Found (404):**
  - Exception: `NoSuchElementException`
  - Response: Book or patron not found.

- **Bad Request (400):**
  - Exception: `IllegalStateException`
  - Response: Book has not been returned yet.

- **Internal Server Error (500):**
  - Exception: `Exception`
  - Response: An unexpected error occurred.

#### Returning Controller:

- **Not Found (404):**
  - Exception: `NoSuchElementException`
  - Response: Book or patron not found.

- **Bad Request (400):**
  - Exception: `IllegalStateException`
  - Response: Book has already been returned.

- **Internal Server Error (500):**
  - Exception: `Exception`
  - Response: An unexpected error occurred.

These standardized exception responses ensure clear communication of errors to API consumers. For more details on specific exceptions, please refer to the respective controller implementations.

### Logging:

Custom logging is implemented to capture method entry and exit points for controllers.

#### `LogExecution` Annotation:

To enable logging for a specific method, annotate it with `@LogExecution`.

#### `LoggingAspect` Functions:

- `logBefore(JoinPoint joinPoint)`: Logs method entry for controllers with arguments.
- `logAfter(JoinPoint joinPoint)`: Logs method exit for controllers.

#### Additional Logging Info:

- **Borrowing Controller:**
  - Logs when a patron is sending a request to borrow a book with specific IDs.

- **Patron Controller:**
  - Logs when a user is fetching all users.
  - Logs when a new user is registered with a specific username.
  - Logs when a user is authenticated and updating their profile.

- **Returning Controller:**
  - Logs when a patron is sending a request to return a book with specific IDs.

This logging setup, combined with additional information, enhances traceability and aids in debugging and performance analysis. To enable logging for a specific method, simply annotate it with `@LogExecution`.

### Transactional Management:

In the Borrowing Service, `@Transactional` is applied to `borrowBook` and `returnBook`, ensuring atomic execution of multiple database operations.
These methods include multiple database operations, such as retrieving book and patron details, updating borrowing records, and saving changes.

## Testing

### Overview
This section covers unit testing for controllers and repositories in a Spring Boot project using the H2 in-memory database for testing.

### Controllers Testing
- **BorrowingControllerTests:** Tests for borrowing functionality, including successful borrowing, error handling for book not found, invalid requests, and internal server errors.
- **ReturningControllerTests:** Tests for book return functionality, covering success, book not found, invalid requests, and internal server errors.

### Repository Testing
- **BookRepositoryTests:** Repository tests for book-related operations, such as saving, retrieving, updating, and deleting books.
- **PatronRepositoryTests:** Repository tests for patron-related operations, including saving, retrieving, updating, and deleting patrons.

### Testing Properties
- **Database Configuration:** Uses an H2 in-memory database with PostgreSQL compatibility.
- **URL:** `jdbc:h2:mem:test_db;MODE=PostgreSQL;`
- **Driver:** `org.h2.Driver`
- **Hibernate Dialect:** `org.hibernate.dialect.PostgreSQLDialect`

### Annotations Used
- **`@ExtendWith(MockitoExtension.class)`:**
  - Extends JUnit 5 testing model for Mockito integration.

- **`@SpringBootTest`:**
  - Loads the class with Spring Boot's testing support for integration testing.

- **`@AutoConfigureMockMvc`:**
  - Automatically configures `MockMvc` for testing Spring MVC controllers.

- **`@WithMockUser`:**
  - Simulates a user in the test context, facilitating authentication testing.


### Mocking Frameworks
- **MockMvc:** Simulates HTTP requests and responses, allowing the testing of Spring MVC controllers in isolation.
- **MockBean:** Used to mock Spring beans when testing, providing control over the bean's behavior during testing.

### Note
- The tests are isolated to prevent interference with each other.
- MockMvc is employed for simulating HTTP requests and responses.
- MockBean is utilized for mocking Spring beans, enabling controlled testing of components.

## Tools

- **JWT Dependencies (jjwt-api, jjwt-impl, jjwt-jackson):** JSON Web Token support for authentication and authorization.

- **PostgreSQL Driver:** Connectivity to a PostgreSQL database.

- **Project Lombok:** Boilerplate code reduction with annotations.

- **H2 Database:** In-memory database for testing.

- **Mockito:** Mocking framework for test mocking and isolation.

- **JUnit:** Testing framework for Java applications.
