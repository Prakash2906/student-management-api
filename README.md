# Student Management API

A RESTful CRUD API for managing students, built with **Spring Boot 3**, **Spring Data JPA** and **Bean Validation**. Runs out of the box on an in-memory H2 database, with an optional MySQL profile.

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen) ![MySQL](https://img.shields.io/badge/MySQL-optional-blue)

## Features

- Full CRUD for students (create, read, update, delete)
- Filter by course or search by name
- Input validation with clear, field-level error messages
- Duplicate email detection (409 Conflict)
- Centralized exception handling with `@RestControllerAdvice`
- Layered architecture: Controller → Service → Repository
- Integration tests with MockMvc

## Tech Stack

Java 17 · Spring Boot · Spring Data JPA · Hibernate · H2 · MySQL · JUnit 5 · Maven

## Project Structure

```
src/main/java/com/prakash/studentapi
├── controller/   StudentController       REST endpoints
├── service/      StudentService          business logic
├── repository/   StudentRepository       Spring Data JPA
├── model/        Student                 JPA entity + validation
└── exception/    GlobalExceptionHandler  consistent error responses
```

## Getting Started

```bash
git clone https://github.com/Prakash2906/student-management-api.git
cd student-management-api
mvn spring-boot:run
```

The API starts at `http://localhost:8080`. H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:studentdb`).

### Using MySQL

```bash
DB_USERNAME=root DB_PASSWORD=yourpassword mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### Run tests

```bash
mvn test
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/students` | List all students |
| GET | `/api/students?course=CSE` | Filter by course |
| GET | `/api/students?name=asha` | Search by name |
| GET | `/api/students/{id}` | Get a student by id |
| POST | `/api/students` | Create a student |
| PUT | `/api/students/{id}` | Update a student |
| DELETE | `/api/students/{id}` | Delete a student |

### Example

```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Asha Rao","email":"asha@example.com","course":"CSE","year":3}'
```

Validation error response:

```json
{
  "timestamp": "2026-10-13T10:15:30",
  "status": 400,
  "error": "Validation failed",
  "details": { "email": "Email must be valid", "year": "Year must be between 1 and 5" }
}
```

## Author

**Prakash H** · [GitHub](https://github.com/Prakash2906)
