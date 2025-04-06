
# User Onboarding System

## Overview

The **User Onboarding System** is a microservices-based platform designed to manage user registration, authentication, and role-based access control. It consists of three key microservices:

1. **User Management Service** – Handles user registration, password updates, user deletions, and role management.
2. **Session Management Service** – Manages JWT session creation, role-based access control (RBAC), password expiry, and validates the user's security header.
3. **OAuth Service** – Provides OAuth 2.0 authentication for external clients, enabling users to log in or register via third-party providers like Google or Facebook.

The system uses **JWT tokens** for user sessions and **PostgreSQL** as the database. **Kubernetes** is used for container orchestration and deployment.

---

## Tech Stack

- **Backend Framework**: Spring Boot
- **Authentication**: JWT (JSON Web Tokens)
- **OAuth 2.0**: Custom OAuth provider for user login via external providers (Google, Facebook, etc.)
- **Database**: PostgreSQL (internal with Kubernetes)
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **Security**: Spring Security for role-based access control (RBAC)
- **API Documentation**: Swagger/OpenAPI
- **Testing**: JUnit, Mockito
- **gRPC**: For inter-service communication (e.g., user suspension)

---

## Microservices Overview

### 1. User Management Service
- **Responsibilities**:
  - User registration, password updates, and deletions
  - Role management (creating, updating, and assigning roles)
  - gRPC interface for suspending users based on inactivity
- **Endpoints**:
  - `POST /users` – Register a new user
  - `PATCH /users/{userId}/password` – Update user password
  - `DELETE /users/{userId}` – Delete a user
  - `POST /users/{userId}/roles` – Assign or update roles for a user

### 2. Session Management Service
- **Responsibilities**:
  - Creates a new session for the user using JWT
  - Validates the JWT session token
  - Verifies user roles before granting access to resources
  - Checks for password expiry and forces password changes if the password hasn’t been updated in 30 days
- **Endpoints**:
  - `POST /sessions` – Create a new session with JWT token
  - `POST /sessions/validate` – Validate an existing session and check for expiry
  - `GET /resources/{resourceId}/access` – Validate if the user has access to a resource based on roles

### 3. OAuth Service
- **Responsibilities**:
  - Provides OAuth authentication for external clients
  - Allows users to log in or register using third-party OAuth providers (Google, Facebook)
  - Manages OAuth token exchange and session creation for users
  - Issues OAuth access tokens, validates token usage, and manages user session information
- **Endpoints**:
  - `POST /oauth/authorize` – Initiate OAuth login flow
  - `POST /oauth/token` – Exchange authorization code for access token
  - `POST /oauth/refresh` – Refresh an access token using a refresh token
  - `GET /oauth/userinfo` – Retrieve user information associated with the provided access token

---

## Database Schema

### PostgreSQL Tables
- **`users`** - Stores user details (first name, last name, email, password, status, etc.)
- **`roles`** - Stores roles (ADMIN, USER, etc.)
- **`role_resource_access`** - Stores the resource-role mapping for access control.
- **`sessions`** - Stores session-related data (like JWT tokens, expiration, and user details associated with the session).

### Database Initialization Jobs
- **Kubernetes Job**: A Kubernetes job will apply Flyway migrations to initialize the database schema during deployment.

---

## Local Development Setup

To run the application locally, you'll need to set up **PostgreSQL** with Docker and configure the Spring Boot application for local development.

### Step 1: Set Up PostgreSQL with Docker

1. **Pull the PostgreSQL Docker Image**:
   Run the following command to pull the PostgreSQL image:
   ```bash
   docker pull postgres:latest
   ```

2. **Run PostgreSQL in Docker**:
   Run the following command to start a PostgreSQL container with a custom database, username, and password:
   ```bash
   docker run --name postgres-container -e POSTGRES_DB=user_onboarding_system -e POSTGRES_USER=yourusername -e POSTGRES_PASSWORD=yourpassword -p 5432:5432 -d postgres:latest
   ```
  - Replace `yourusername` and `yourpassword` with your desired PostgreSQL username and password.
  - The database `user_onboarding_system` will be created automatically.

3. **Verify the PostgreSQL Container**:
   To verify if the container is running, use:
   ```bash
   docker ps
   ```
   This should show the PostgreSQL container running, mapped to port `5432`.

### Step 2: Configure Spring Boot Application

1. **Edit `application.properties`**:
   In your Spring Boot project, configure the database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/user_onboarding_system
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.hibernate.ddl-auto=none  # Use Flyway for schema creation
   spring.flyway.enabled=true           # Enable Flyway migration
   spring.flyway.locations=classpath:db/migration  # Location of migration scripts
   ```

### Step 3: Set Up Flyway Migrations

1. **Create Migration Scripts**:
   Create a folder `db/migration` in `src/main/resources`. Inside it, create SQL migration files like `V1__Create_tables.sql` to initialize the database schema.

   Example `V1__Create_tables.sql`:
   ```sql
   CREATE TABLE IF NOT EXISTS users (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       first_name VARCHAR(255),
       last_name VARCHAR(255),
       email VARCHAR(255) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       status VARCHAR(50) DEFAULT 'REGISTERED',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   
   CREATE TABLE IF NOT EXISTS roles (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       name VARCHAR(50) NOT NULL UNIQUE,
       description TEXT
   );
   ```

2. **Flyway Migration**:
   When you run the Spring Boot application, Flyway will automatically run the migrations and apply them to the PostgreSQL database.

### Step 4: Run the Spring Boot Application

1. **Run the Application**:
   In your terminal, use:
   ```bash
   mvn spring-boot:run
   ```

2. **Verify Database**:
   After running the application, verify the tables were created in PostgreSQL using **psql** or **pgAdmin**.

---

## API Documentation

Once the application is running, you can access the API documentation at:
```bash
http://localhost:8080/swagger-ui.html
```

The API is documented using **Swagger** and **OpenAPI** specifications.

---

## Testing

Unit and integration tests are written using **JUnit** and **Mockito**. To run tests:
```bash
mvn clean install
mvn test
```

---

## Contributing

1. Fork this repository
2. Create a feature branch (`git checkout -b feature-branch`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature-branch`)
5. Open a pull request

---
