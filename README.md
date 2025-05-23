# Simple Task Manager API

A RESTful API for managing tasks built with Spring Boot and PostgreSQL.

## Technologies Used

- Java 24
- Spring Boot 3.4.5
- PostgreSQL
- Maven
- Spring Data JPA
- Spring Validation

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven
- PostgreSQL

### Database Setup

1. Create a PostgreSQL database named `taskmanager`:

```sql
CREATE DATABASE taskmanager;
```

2. Update the database configuration in `src/main/resources/application.properties` if needed.

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Maven:

```
mvn spring-boot:run
```

The application will start on port 8080.

## API Endpoints

### Create a Task

- **URL**: POST /api/tasks
- **Request Body**:

```json
{
  "title": "Complete assignment",
  "description": "Finish the Spring Boot task manager API",
  "status": "PENDING",
  "dueDate": "2023-06-30"
}
```

- **Response**: 201 Created with the created task

### Get All Tasks

- **URL**: GET /api/tasks
- **Response**: 200 OK with an array of tasks

### Get Task by ID

- **URL**: GET /api/tasks/{id}
- **Response**: 200 OK with the task or 404 Not Found

### Update Task

- **URL**: PUT /api/tasks/{id}
- **Request Body**:

```json
{
  "title": "Updated task title",
  "status": "IN_PROGRESS"
}
```

- **Response**: 200 OK with the updated task or 404 Not Found

### Delete Task

- **URL**: DELETE /api/tasks/{id}
- **Response**: 204 No Content or 404 Not Found

## Data Model

### Task

- **id**: Long (auto-generated)
- **title**: String (required)
- **description**: String
- **status**: Enum (PENDING, IN_PROGRESS, COMPLETED)
- **dueDate**: LocalDate
- **createdAt**: LocalDateTime (auto-generated)
- **updatedAt**: LocalDateTime (auto-updated)
