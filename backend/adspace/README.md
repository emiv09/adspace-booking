# Ad Space Booking System - Backend API

A RESTful backend API for managing ad space bookings built with Spring Boot 3.5.8 and Java 17.

## Features & Business Rules

- **Ad Space Management**: List and view available advertising spaces.
- **Booking System**: Create, approve, and reject booking requests.
- **Validation**: Enforces business rules such as minimum booking duration (7 days), future start dates, and prevention of overlapping approved bookings.
- **Comprehensive Error Handling**: Provides structured JSON error responses.
- **Testing**: Includes a full suite of unit and integration tests.

## Prerequisites

- Java 17 or higher
- Docker (for PostgreSQL)
- Maven 3.6+ (or use the included wrapper)

## Setup & Configuration

1.  **Start PostgreSQL using Docker**:
    Run the following command to start a PostgreSQL container. Replace `<your-password>` with a password of your choice.
    ```bash
    docker run --name postgres-challenge -e POSTGRES_PASSWORD=<your-password> -p 5432:5432 -d postgres:15
    ```
    *Note: The default user and database for the `postgres` image are both `postgres`.*

2.  **Configure Environment**:
    - Copy the `.env.example` file to a new file named `.env`.
    - Update the `DB_PASSWORD` in your `.env` file to match the password you set in the Docker command.
    - The application will automatically create the required tables when it first starts.

## Build and Run

The application is configured to run on `http://localhost:8080`.

### Run the Application
```bash
# Using the Maven wrapper (recommended)
./mvnw spring-boot:run
```

### Run Tests
```bash
./mvnw test
```

## API Endpoints

All endpoints are prefixed with `/api/v1`.

### Ad Spaces
- `GET /ad-spaces`: List available ad spaces.
  - Query Params: `type`, `city`
- `GET /ad-spaces/{id}`: Get details of a single ad space.

### Booking Requests
- `POST /booking-requests`: Create a new booking request.
- `GET /booking-requests`: List all booking requests.
  - Query Param: `status` (PENDING, APPROVED, REJECTED)
- `GET /booking-requests/{id}`: Get booking details.
- `PATCH /booking-requests/{id}/approve`: Approve a pending booking.
- `PATCH /booking-requests/{id}/reject`: Reject a pending booking.

*For detailed request/response examples, see the `API_REFERENCE.md` file or import the `postman_collection.json` into Postman.*

## Technology Stack

- **Java 17** & **Spring Boot 3.5.8**
- **Spring Data JPA** & **Hibernate**
- **PostgreSQL** (Production) & **H2** (Testing)
- **Maven**, **JUnit 5**, **Mockito**
- **Lombok**

## Project Structure

The project follows a standard layered architecture:
- `controller`: REST API endpoints
- `service`: Business logic
- `repository`: Data access layer
- `entity`: JPA entities
- `dto`: Data Transfer Objects
- `exception`: Custom exceptions and global handler
- `config`: Application configuration (CORS)

